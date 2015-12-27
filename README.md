# JaySound
Experimental Java sound library for OpenAL (via LWJGL). The API is still very unstable and changes often.

## Installation
JaySound is currently not in any maven repositories. Easiest way to install it is probably cloning the repo and doing a local `mvn install`.

## Usage examples
Note: all examples require LWJGL natives on `java.library.path`.

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

__3D sound revolving around the listener__
```java
AudioContext ctx = new AudioContext();

Sound sound = ctx.createStreamingSound(new URL("http://stream.plusfm.net/;"));
sound.play();

while (true) {
    ctx.update();

    double time = (System.currentTimeMillis() / 300.0);
    sound.setPosition((float) Math.cos(time), 0f, (float) Math.sin(time));

    Thread.sleep(30);
}
```

__Read stream title (which is the currently playing track for most streams)__
```java
AudioContext ctx = new AudioContext();

Sound sound = ctx.createStreamingSound(new URL("http://stream.plusfm.net/;"));
sound.play();

String printedTitle = null;
while (true) {
    ctx.update();
    
    String decodedTitle = sound.getDecodedTitle();
    if (!Objects.equals(decodedTitle, printedTitle)) {
        System.out.println(decodedTitle);
        printedTitle = decodedTitle;
    }
    
    Thread.sleep(30);
}
```