# CP372 Assignment 1 – Bulletin Board System

## Quick Start (Windows/PowerShell)

This project comes with helper scripts to make building and running easy. You will need **Java JDK 17** or higher installed.

### 1. Start the Server

Open a terminal in the project root (`cp372a1`) and run:

```powershell
.\run-server.ps1
```

This will:

- Automatically **compile** the project (if not already built).
- Start the **server** on port `6767`.
- Set up a default board (400x300) with Red, Blue, and Green notes.

### 2. Start the Client

Open a **second** terminal window in the project root and run:

```powershell
.\run-client.ps1
```

This will launch the "JJ's Bulletin Board" GUI and connect to your local server. You can run this command multiple times in different terminals to launch multiple clients.

---

## Manual Instructions

If you prefer to run commands manually or use a different shell:

### 1. Build

```powershell
# Create directories
New-Item -ItemType Directory -Force -Path build\shared, build\server, build\client

# Compile
javac -d build\shared shared\src\main\java\shared\*.java shared\src\main\java\utils\*.java
javac -cp build\shared -d build\server server\src\main\java\server\*.java server\src\main\java\server\utils\*.java
javac -cp build\shared -d build\client client\src\main\java\client\*.java client\src\main\java\client\gui\*.java
```

### 2. Run Server

```powershell
# Usage: port board_w board_h note_w note_h colour1 ...
java -cp "build\server;build\shared" server.ServerMain 6767 400 300 80 60 red blue green
```

### 3. Run Client

```powershell
java -cp "build\client;build\shared" client.ClientMain
```
