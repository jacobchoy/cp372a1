# CP372 A1 - Run client
# Starts the bulletin board GUI client. Connects to localhost:6767 by default.
# Start the server first with run-server.ps1.

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot

if (-not (Test-Path "$root\build\client\client\ClientMain.class")) {
    Write-Host "Not built yet. Running build.ps1..."
    & "$root\build.ps1"
}

Write-Host "Starting client (connect to localhost:6767)..."
java -cp "$root\build\client;$root\build\shared" client.ClientMain $args
