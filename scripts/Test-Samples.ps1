[CmdletBinding()]
param([string[]]$Sample)

$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$smoke = Join-Path $PSScriptRoot 'smoke-samples.mjs'
$allSamples = @(& node $smoke --list)
if ($LASTEXITCODE -ne 0) { throw 'Could not list sample checks.' }
$selected = if ($Sample) { $Sample } else { $allSamples }
foreach ($name in $selected) {
    if ($name -notin $allSamples) { throw "Unknown sample: $name" }
}

$linuxPreviews = @()
if ($env:OS -eq 'Windows_NT') {
    $linuxPreviews = @($selected | Where-Object {
        $_ -in @('preview/svelte-go-bookmarks', 'preview/spring-boot-postgres')
    })
}
$native = @($selected | Where-Object { $_ -notin $linuxPreviews })
$nativeFailed = $false
if ($native.Count) {
    & node --use-system-ca $smoke @native
    $nativeFailed = $LASTEXITCODE -ne 0
}
if (-not $linuxPreviews.Count) {
    if ($nativeFailed) { throw 'Native sample validation failed.' }
    return
}

$dockerfile = Join-Path $PSScriptRoot 'linux-previews.Dockerfile'
$image = 'aspire-polyglot-linux-previews:13.6.1'
& docker build --file $dockerfile --tag $image $root
if ($LASTEXITCODE -ne 0) { throw 'Linux validation image build failed.' }

$project = 'apv-' + [guid]::NewGuid().ToString('N').Substring(0, 12)
$compose = @('compose', '--project-name', $project, '--file',
    (Join-Path $PSScriptRoot 'linux-previews.compose.yml'))
try {
    & docker @compose up --detach --wait --wait-timeout 180 docker
    if ($LASTEXITCODE -ne 0) { throw 'The private Linux Docker daemon did not become healthy.' }
    & docker @compose run --rm --no-deps -T runner @linuxPreviews
    if ($LASTEXITCODE -ne 0) { throw 'Linux preview validation failed.' }
}
finally {
    & docker @compose down --volumes
    if ($LASTEXITCODE -ne 0) { throw "Could not clean up validation project $project." }
}
if ($nativeFailed) { throw 'Native checks failed; Linux preview checks completed successfully.' }
Write-Output "$($selected.Count)/$($selected.Count) samples passed ($($native.Count) native, $($linuxPreviews.Count) Linux)."
