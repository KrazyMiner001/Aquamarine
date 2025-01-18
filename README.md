# Aquamarine
Multiblock library to allow creation of multiblocks in mods easier.
This mod help create "Modern Industrialization-like" multiblocks, i.e. multiblocks which have a controller, some innert blocks, and hatch blocks for IO.

Note: This project is very unfinished but works. This is intended as a base library for my other mods using multiblock but can be used by others.

Docs exist but not for everything, still WIP.

## Features
### Multiblocks
Multiblocks are made of lists both simple members and hatch flags. The simple member defines what block goes at that position. The hatch flags define what hatch types can replace that position. Empty flags can be provided.

Multiblocks can easily access their attached hatches to transfer items/fluids/energy between multiblock and hatch.

### Multiblock Rendering
#### Rendering multiblock previews layer by layer.
![Layered Multiblock Rendering](https://cdn.modrinth.com/data/cached_images/99cc78f26c19981748bea96f5745cf359318a8a0_0.webp)

#### Indicating which blocks can be replaced by hatches.
If a block in the multiblock can be replaced by a hatch that the player is holding, the block will have a green outline around it.
![Replace this with a description](https://cdn.modrinth.com/data/cached_images/ad497af49f829a580656b55b1d1366eb57772845.png)

#### Rendering entire multiblock at one.
Not recommended as it renders weirdly and layer by layer is easier to build.

## How to use
### Adding dependancy
Add version to gradle.properties
``` properties
aquamarine_version = 0.0.1-1.21.3 # replace with version id from download page
```

Add repository to build.gradle
``` groovy
repositories {
  maven { url 'https://jitpack.io' }
}
```

Add dependency to build.gradle
``` groovy
dependencies {
    modApi "com.github.KrazyMiner001:Aquamarine:${project.aquamarine_version}"
}
```

### Using
Create a class which extends MultiblockBlockEntity and implements methods.
Create a class which extends HatchBlockEntity and implements methods.
Create blocks for both block entities.
Multiblock.
(better explanation todo)
