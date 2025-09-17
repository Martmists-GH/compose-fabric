# Compose-Fabric

This is a snippet for how to use Jetbrains Compose Multiplatform to show Minecraft Display Entities.

## Preview

The following is a preview of the [ComposeTestMod](./src/testmod/kotlin/com/martmists/compose/testmod/ComposeTestMod.kt).

![A video showing the animated contents of the sample snippet](./.github/preview.mp4)

## Usage

Compose displays must be bound to an entity, and must explicitly add players to watch the displays.

```kotlin
val entity: Entity = ...
val player: ServerPlayerEntity = ...

val holder = entity.openDisplayGui {
    // Place Compose code here
}
holder.startWatching(player)
```

> Note: There are a few caveats to using the example snippet in [ComposeTestMod](./src/testmod/kotlin/com/martmists/compose/testmod/ComposeTestMod.kt) noted near the bottom of the file. Keep these in mind when adding the code to your project.

## Contributing

While this project aims to serve as just a proof-of-concept, if there are components or improvements you want to add which you think would be beneficial to others, feel free to open a PR.

Unless it requires significant changes the example code, I am unlikely to merge pull requests for version updates, as this is only a proof-of-concept.

## License

This project is licensed under the [BSD-3-Clause-NON-AI](./LICENSE) license. The TL;DR:
- You are free to copy-paste this code in your own project, as long as you properly disclose it includes this code. This includes adding the LICENSE file to any releases made using this code.
- You may under no circumstances use the code in this repository to train AI.

## Acknowledgements

This project would not have been possible without:
- The FabricMC community, for helping with a bunch of issues I ran into.
- [ComposePPT](https://github.com/fgiris/composePPT) for giving a small example of the bare minimum to create a Compose backend.
- [Mosaic](https://github.com/JakeWharton/mosaic) for giving a more practical example of Compose backends.
