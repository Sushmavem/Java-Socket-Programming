# Java-Socket-Programming
Java Socket Programming Example

This project demonstrates client-server communication using Java sockets.
The server sends the current date, and the client receives and displays it.

# ==> Assignment 2 – Multi-Threaded Java Socket Programming

## Overview
This project implements a multi-threaded chat application using Java Socket Programming.  
The server allows multiple clients to connect simultaneously.  
Each client is handled using a separate thread.  
Clients must enter a unique name before joining the chat.

All client messages are displayed on the server and broadcast to all connected clients.  
The server can also send messages to all clients.

---

## Features
- Multi-threaded server using Thread
- Multiple clients can connect at the same time
- Unique client name validation (no duplicate names)
- Messages broadcast to all connected clients
- Client information stored using HashMap

---

## Files
- `MultiClientServer.java` – Server program
- `MultiClient.java` – Client program

---

## How to Compile
javac MultiClientServer.java  
javac MultiClient.java  

---

## How to Run

Start the server:
java MultiClientServer  

Start client(s):
java MultiClient  

If running on different laptops:
- Connect both devices to the same WiFi
- Replace "localhost" in client code with the server IP address

Example:
Socket socket = new Socket("192.168.x.x", 6013);

---

## Author
Sushma Reddy
