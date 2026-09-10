#:sdk Aspire.AppHost.Sdk@13.5.3
#:property AspireUseCliBundle=true

// This sample intentionally uses Aspire's experimental terminal API.
#pragma warning disable ASPIRETERMINAL001

var builder = DistributedApplication.CreateBuilder(args);

// Skip shell startup scripts so the demo does not depend on personal profiles.
if (OperatingSystem.IsWindows())
{
    builder.AddExecutable("shell", "cmd.exe", ".", "/d")
        .WithTerminal();
}
else
{
    builder.AddExecutable("shell", "/bin/bash", ".", "--noprofile", "--norc", "-i")
        .WithTerminal();
}

// Run Node's built-in REPL rather than an application script or web server.
builder.AddExecutable("node-repl", "node", ".", "--interactive")
    .WithTerminal();

builder.Build().Run();
