# CP372 Assignment 1 – Bulletin Board System

## How to run the program

The project has three parts: **shared** (protocol/utilities), **server**, and **client**. There is no Maven/Gradle; use `javac` and `java` from the project root `cp372a1`.

### 1. Compile

From the project root (`c:\Users\pc\Desktop\cp372a1`):

```powershell
# Create output directories (PowerShell)
New-Item -ItemType Directory -Force -Path build\shared, build\server, build\client

# Compile shared first (shared + utils packages)
javac -d build\shared shared\src\main\java\shared\*.java shared\src\main\java\utils\*.java

# Compile server (depends on shared)
javac -cp build\shared -d build\server server\src\main\java\server\*.java server\src\main\java\server\utils\*.java

# Compile client (depends on shared)
javac -cp build\shared -d build\client client\src\main\java\client\*.java client\src\main\java\client\gui\*.java client\src\main\java\client\utils\*.java
```

**Note:** On Linux/macOS use `/` and `:` in classpath, e.g. `build/shared` and `-cp build/shared`.

### 2. Start the server

```powershell
java -cp "build\server;build\shared" server.ServerMain 6767 400 300 80 60 red green blue yellow
```

Arguments: `port board_width board_height note_width note_height colour1 colour2 ...`  
Example: port **6767**, board **400×300**, notes **80×60**, colours **red green blue yellow**.

### 3. Start the client

In a **second** terminal (server must be running):

```powershell
java -cp "build\client;build\shared" client.ClientMain
```

- Default: connects to **localhost:6767**.
- Custom host/port: `client.ClientMain 192.168.1.10 6767`

The client window opens and uses the board dimensions and colours sent by the server.
