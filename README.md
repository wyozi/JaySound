# JaySound
Experimental Java sound library for OpenAL (via LWJGL). The API is still very unstable and changes often.

## Installation
JaySound is currently not in any repositories. Easiest way to install it is probably `mvn install`.

## Usage examples
Note: all examples require valid java.library.path to load LWJGL native binaries.

__Basic buffered sound__
```java
AudioContext ctx = new AudioContext();

Sound sound = ctx.createBufferedSound(new URL("https://upload.wikimedia.org/wikipedia/en/3/3d/Sample_of_Daft_Punk's_Da_Funk.ogg"));
sound.play();
Thread.sleep(3000);

ctx.dispose();
```

__Basic streaming sound__
```java
AudioContext ctx = new AudioContext();

Sound sound = ctx.createStreamingSound(new URL("https://upload.wikimedia.org/wikipedia/en/3/3d/Sample_of_Daft_Punk's_Da_Funk.ogg"));
sound.play();

// simulate a game loop
for (int i = 0;i < 100; i++) {
    // streaming sound requires constant calls to ctx.update()
    ctx.update();
    
    Thread.sleep(30);
}

ctx.dispose();
```