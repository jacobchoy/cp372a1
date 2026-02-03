# CP372 A1 - Run server
# Starts the bulletin board server on port 6767 (400x300 board, 80x60 notes, 4 colours).
# Run this first; then run run-client.ps1 in another terminal.

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot

if (-not (Test-Path "$root\build\server\server\ServerMain.class")) {
    Write-Host "Not built yet. Running build.ps1..."
    & "$root\build.ps1"
}

Write-Host "Starting server on port 6767..."
java -cp "$root\build\server;$root\build\shared" server.ServerMain 6767 400 300 80 60 red blue green
