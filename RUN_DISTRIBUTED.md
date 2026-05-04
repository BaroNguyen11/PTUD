# Distributed Run Guide

## 1. Compile

```powershell
.\compile.ps1
```

Equivalent manual command:

```powershell
$files = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "lib/*" -d out $files
```

## 2. Start MongoDB

Make sure MongoDB is running and the database exists:

```text
mongodb://localhost:27017
QLNhaHang2BTCHECK
```

Override these values when starting the server if needed.

## 3. Start RMI Server

This starts the server in the background and returns immediately:

```powershell
.\start-server.ps1
```

Custom port or database:

```powershell
.\start-server.ps1 -Port 1099 -MongoUri "mongodb://localhost:27017" -MongoDatabase "QLNhaHang2BTCHECK"
```

Server logs:

```powershell
Get-Content .\server-test.log -Wait
Get-Content .\server-test.err.log -Wait
```

During the demo, RMI calls appear in `server-test.log`, for example:

```text
[RMI] MonAnRemoteImpl.getAllMonAn called by client params=[]
[RMI] MonAnRemoteImpl.getAllMonAn result=[...]
```

## 4. Start Client

Run the JavaFX client after the server is running:

```powershell
.\start-client.ps1
```

Custom RMI target:

```powershell
.\start-client.ps1 -RmiHost localhost -RmiPort 1099
```

The client uses RMI only. Database access is guarded server-side and requires:

```text
app.role=server
```

## Manual Commands

Start server manually:

```powershell
java -Dapp.role=server -Dmongo.uri="mongodb://localhost:27017" -Dmongo.database="QLNhaHang2BTCHECK" -cp "out;lib/*" server.ServerMain 1099
```

Start client manually:

```powershell
java -Dapp.role=client -Drmi.host=localhost -Drmi.port=1099 -cp "out;lib/*" client.application.Launcher
```
