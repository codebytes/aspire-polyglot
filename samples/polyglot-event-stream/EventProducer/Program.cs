using System.Text.Json;
using Confluent.Kafka;

var builder = WebApplication.CreateBuilder(args);

builder.AddServiceDefaults();
builder.AddKafkaProducer<string, string>("messaging");

builder.Services.AddHostedService<SensorEventProducer>();
builder.Services.AddSingleton<SensorConfiguration>();

var app = builder.Build();

app.MapDefaultEndpoints();

app.MapGet("/api/sensors", (SensorConfiguration config) =>
{
    return Results.Ok(config.Sensors);
});

app.MapPost("/api/events", async (SensorEvent sensorEvent, IProducer<string, string> producer) =>
{
    var json = JsonSerializer.Serialize(sensorEvent);
    await producer.ProduceAsync("sensor-readings", new Message<string, string>
    {
        Key = sensorEvent.SensorId,
        Value = json
    });
    
    return Results.Ok(new { status = "published", sensorEvent });
});

app.Run();

public class SensorEventProducer : BackgroundService
{
    private readonly IProducer<string, string> _producer;
    private readonly SensorConfiguration _config;
    private readonly ILogger<SensorEventProducer> _logger;
    private readonly Random _random = new();

    public SensorEventProducer(
        IProducer<string, string> producer,
        SensorConfiguration config,
        ILogger<SensorEventProducer> logger)
    {
        _producer = producer;
        _config = config;
        _logger = logger;
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        _logger.LogInformation("Sensor Event Producer starting...");

        while (!stoppingToken.IsCancellationRequested)
        {
            foreach (var sensor in _config.Sensors)
            {
                var sensorEvent = new SensorEvent
                {
                    SensorId = sensor.Id,
                    Temperature = sensor.BaseTemperature + (_random.NextDouble() * 10 - 5), // ±5°C variation
                    Humidity = sensor.BaseHumidity + (_random.NextDouble() * 20 - 10), // ±10% variation
                    Location = sensor.Location,
                    Timestamp = DateTime.UtcNow
                };

                var json = JsonSerializer.Serialize(sensorEvent);
                
                try
                {
                    await _producer.ProduceAsync("sensor-readings", new Message<string, string>
                    {
                        Key = sensorEvent.SensorId,
                        Value = json
                    }, stoppingToken);

                    _logger.LogInformation("Published event from {SensorId} at {Location}: {Temp}°C, {Humidity}%",
                        sensorEvent.SensorId, sensorEvent.Location, sensorEvent.Temperature.ToString("F1"), sensorEvent.Humidity.ToString("F1"));
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Failed to publish event for sensor {SensorId}", sensorEvent.SensorId);
                }
            }

            await Task.Delay(TimeSpan.FromSeconds(2), stoppingToken);
        }
    }
}

public class SensorConfiguration
{
    public List<SensorInfo> Sensors { get; } = new()
    {
        new SensorInfo { Id = "sensor-01", Location = "Warehouse A", BaseTemperature = 22, BaseHumidity = 55 },
        new SensorInfo { Id = "sensor-02", Location = "Warehouse B", BaseTemperature = 24, BaseHumidity = 60 },
        new SensorInfo { Id = "sensor-03", Location = "Cold Storage", BaseTemperature = 5, BaseHumidity = 70 },
        new SensorInfo { Id = "sensor-04", Location = "Server Room", BaseTemperature = 20, BaseHumidity = 45 },
        new SensorInfo { Id = "sensor-05", Location = "Production Floor", BaseTemperature = 28, BaseHumidity = 65 }
    };
}

public class SensorInfo
{
    public required string Id { get; set; }
    public required string Location { get; set; }
    public double BaseTemperature { get; set; }
    public double BaseHumidity { get; set; }
}

public class SensorEvent
{
    public required string SensorId { get; set; }
    public double Temperature { get; set; }
    public double Humidity { get; set; }
    public required string Location { get; set; }
    public DateTime Timestamp { get; set; }
}
