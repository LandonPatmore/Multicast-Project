# Final-Project
CSC 495 Final Project - Multicast Project

## How to run

First, to create the backend, go to the root of the directory and type `gradle :backendJar`.  This will create a jar in the `build/libs` folder in the root.

Next, to create the frontend, go into the frontend directory, and type `gradle :frontend:desktop:dist`.  This will create a jar in the `desktop/build/libs` folder.

## Encryption

The encryption algorithm used is AES, running in CBC mode. CBC mode requires byte arrays to be padded, which is done using the PKCS#5 scheme. The mode also requires an initialization vector, which is provided via the SecureRandom class.
CBC mode was used because it compensates for the security flaws that arise from reusing secrets, which happened often.

- encryptByteArray
    The method takes the array containing the plain text, the actual length of the plain text, and the secret.
    A random IV byte array is generated every time before encryption time. After generating the cipher text byte array, it is added onto the end of the IV byte array and returned.

- decryptByteArray
    The method takes the IV-cipherText array, the length of the actual data in the array, and the secret.
    The IV is first extracted from the array, and the rest of the array (the actual cipher text) is decrypted. The decrypted plain text is returned.

## Backend

The server is running on port `4445`.  It uses a DatagramSocket to receive and send packets to the Multicast group.

## Frontend

The frontend is made with libGDX, and is a rendition of a prior project: [Pixel Art](https://github.com/landonp1203/Pixel-Art-libGDX).  It is listening on Multicast group `224.0.0.192` port `4446`.

## Limitations

There are a multitude of limitations:

- If the server goes down, the whole game stops working.  The clients are not able to talk to each other since it is a client-server architecture.
- There is no consensus protocol to make a client the server.
- If a client disconnects, and then reconnects, the client does request the state of the game before they joined.  While it works, it is very slow, either because of the network or because of the implementation, unknown why.

## Tolerances

There is one tolerance issue: if a player decides to spam the game with a large amount of plays a in a small amount of time, the packets are slowly sent for some reason.  Still unknown why this happens.

## Future Improvements

Many improvements can be made:

- While the client does receive error packets, they are not displayed anywhere but the console.  If they are not runnning it from the command line, they will never see the errors.  Being able to show errors from the server would be a great way to inform users what is going on.
- Figure out why the StateRequestPackets are taking forever to send and make it faster to get players up to speed with the latest game state quick.
- Implement a consensus protocol so that clients are the server, and if the server goes down, the client can become a server and continue the game without hiccups.
- Make it better looking because there are some GUI bugs that happen.
- Figure out why if someone spams the game with a lot of plays very quickly are the plays sent/received slowly.

## Contributors

- Landon Patmore | [Github](https://github.com/landonp1203)
- Ye Bhone Myat | [Github](https://github.com/ye-bhone-myat)
- Robert Killmer | [Github](https://github.com/ImPickleRiiicck)
- Benjamin Caro | [Github](https://github.com/BenjiCaro) 