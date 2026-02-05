# CP372 A1 - Build script

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot

Write-Host "Creating build directories..."
New-Item -ItemType Directory -Force -Path "$root\build\shared", "$root\build\server", "$root\build\client" | Out-Null

Write-Host "Compiling shared (from server/shared)..."
javac -d "$root\build\shared" `
    "$root\server\shared\src\main\java\shared\*.java" `
    "$root\server\shared\src\main\java\utils\*.java"

Write-Host "Compiling server..."
javac -cp "$root\build\shared" -d "$root\build\server" `
    "$root\server\src\main\java\server\*.java" `
    "$root\server\src\main\java\server\utils\*.java"

Write-Host "Compiling client..."
javac -cp "$root\build\shared" -d "$root\build\client" `
    "$root\client\src\main\java\client\*.java" `
    "$root\client\src\main\java\client\gui\*.java"

Write-Host "Build complete."
