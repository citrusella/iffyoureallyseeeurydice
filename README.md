# IFF You Really See Eurydice

![Release version shield](https://img.shields.io/github/v/release/citrusella/iffyoureallyseeeurydice)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/citrusella/iffyoureallyseeeurydice/latest)
![Licensed GPL-3.0](https://img.shields.io/github/license/citrusella/iffyoureallyseeeurydice)


## Overview

IFF You Really See Eurydice is a simple program for Sims 1 object hacking that allows a user to convert version 1.0 iff files to version 2.0 iff files. This is intended to allow for finding easier starting points to port prototype objects, especially very complex ones, to the final released game in as accurate a way as possible.

All you need to do is open the program, select your 1.0 iff, verify it seems to have what you might expect in it, select your destination folder and filename, and save it.

### What this project *does do*:

- Creates files that can be opened (and potentially edited) in some existing iff editing tools, like Iff Pencil 2, Codex, Script Station, Volcanic, etc. (I imagine also IffSnooper if you're on Mac. But I'm not on Mac, so I can't say for sure.)
- Converts files from version 1.0 to 2.0 verbatim wherever possible (the file structure is kept the same except for required structural changes and the leaving out of resources not needed to fit those new structural changes)
- Converts iff and spf files (though it converts them both to the iff file ending due to apparent limitations in the file picking library used in the project)
  - It also can convert stx files, but I haven't come across any, it's just an allowed file type for the file to convert from
- Provides comments for most lines in the source code that I wrote so that you understand why I put them there if you go looking or want to contribute

### What this project *does not do*:

- Automatically make a prototype iff work correctly in the final game. **That's still up to you.** It's just for making the files readable/editable in existing tools that can handle 2.0 iff files. Nearly all prototype objects need *some* sort of edit to work in an expected manner in the final game. (Common ways resources may be incompatible are discussed further down in the readme.)
- Make an editor properly read and understand a version of a resource in the iff that it is not completely equipped to understand. For example, Iff Pencil 2 chugs along with most versions of most resources that could make their way into an iff converted this way. (It can even sometimes (OBJD, TTAB maybe) at least partially upgrade them to a newer version by opening and saving them as well.) On the other hand, prototype TTABs will at least sometimes not appear at all in Volcanic, and those same TTABs will fully crash Script Station. This is just one example, though. (Again, some are discussed further down in the readme.)
- Convert to version 2.5. I just didn't want to worry about handling the rsmp resource 2.5 supports. (Iff Pencil 2 (and maybe other editors) may automatically adjust a 2.0 iff into a 2.5 iff upon saving them, though.)
- Convert a 2.0 or 2.5 to a 1.0. It's not for that.
- Protect you from accidentally overwriting another iff, beyond what your operating system's default file manager will do. (So just be careful. There were a couple times during testing I nearly overwrote a 1.0 iff because I didn't think about which step of the process I was in.)
- Combine an iff and a companion spf into a unified iff (though you can do this yourself outside this program). Every file this application creates is a file made from scratch. Saving a later conversion over an earlier conversion's file name *will* replace its contents completely.

...That sounds like a lot. What you need to know is that all this really does (and all it's intended to do) is turn a 1.0 into a 2.0, but making that 2.0 *work correctly* is up to you.

### I'm sold. How do I run and use it?

Visit the [Releases page](https://github.com/citrusella/iffyoureallyseeeurydice/releases) and download the build for your OS. It's not an installed program, so you can put it anywhere and run it. It comes with an executable and a simplified readme containing the bits of this readme that are relevant to helping you remember how the program works. Executables are available for Windows and Linux.

Start the program, and you'll be greeted with a couple of welcome sentences explaining what the program is and what to do.

Select the 1.0 iff with the first button. It will allow iff, spf, or stx files, though only the first two seem to exist in prototypes. If you don't pick a 1.0 iff, it will show you a red error message.

Once you pick a 1.0 iff, the program will load a list of the resources it sees in the file for you to double-check yourself and enable the button for picking a file to save to. Make sure these look like the kinds of resources you expect to see inside an iff. (If you don't know what resources go in an iff, you might want to learn more about Sims 1 object hacking before using this program.)

You might see two resource types you don't normally see. XXXX resources can exist even in 2.0/2.5 iff files, but they tend to contain garbage data. Object hacking programs like Iff Pencil 2 may not display them to you and may strip them out automatically on saving. This converter will write them to the file in the name of accuracy, but be aware that other programs may remove them in the course of your edits to the object. NAME resources are 1.0 specific resources that contain a list of all labels used by other resources in the file, and most files have only one resource named this, though some may have two. This converter will not write NAME resources themselves to the file but will use the last NAME chunk in the file to properly label the other resources in the way that a 2.0 iff expects.

Select a destination folder and file name with the second button. This will write a new file, so make sure not to overwrite an existing one unless you mean to replace it with a new file. When the converter believes itself to be done, you will see a message that the file has been converted, listing the path the file was saved to.

#### Common incompatibilities between prototype objects and the final game

This is **not** an exhaustive list.

- Prototype animations never work because they do not exist in the final game (and were designed with a different "skeleton" in mind so they wouldn't work anyway). These always need to be replaced with a new animation/equivalent final game animation.
- Sounds will not work as written. Prototype sounds can be ported as well, or the sound can be replaced with an existing sound in the game.
- OBJDs are version 136 rather than 138. They must be upgraded to version 138. There may be more than one way to properly upgrade it.
- TTABs are an old version and need to be upgraded. There may be more than one way to do this.
- GUIDs have a *high likelihood* of conflicting with later Maxis objects, because many base game and even in some cases EP objects are more polished revisions of these prototype objects. Clone it or something--and make sure you have a magic cookie if you intend to share!--unless your intent is to create a default replacement for some reason.
- Most editors are not equipped to edit prototype catalog resources (CATS), or at least they are not equipped to edit them in a nice pretty GUI. Transmogrifier can do so but in so doing will convert it to a CTSS like the final game uses. (This may be desired if you want a translatable catalog name and description and is an easy enough conversion to do manually if you don't want T-mog to do it.)
- Iff Pencil 2 does not recognize SPR# resources in the DGRP viewer/editor (even ones in some Maxis-provided final game objects like the pedestrian portal), but the SPR# will work in-game.
- It is possible for an object that uses SPR# instead of SPR2 to crash some tools that can display it, like T-mog or Sim Explorer. Generally if an object does this, it will *also* crash the game itself, if the game tries to load it. (This is also *usually* due to an incorrect flag in the chunk header, which this tool shouldn't be capable of messing up.)
- Of note: Some objects need very minimal edits to work. Others (like floors or houses, for example) may require extensive reworking in order to run properly in the game or may never work using this method.

### Okay, but why the name IFF You Really See Eurydice?

Well, first, there's this song in build mode called ["If You Really See Eurydice"](https://www.youtube.com/watch?v=OAJIATuky28), which is really a good enough reason on its own to make the pun, and I'm surprised I seem to be the first.

Secondly, the story of Eurydice from Greek myth is that she stepped on a viper and died, and Orpheus (her husband) played a song so sad that it basically helped him travel to the Underworld and retrieve her. There's more to that story (like Eurydice not actually being revived and Orpheus getting killed), but the parallel between bringing someone back to life and bringing a 1.0 file "back" in part by converting it to 2.0 was *also* too good to pass up.

Honestly? This is one of the best software names I've come up with. I'm surprised it fits so well.

### To do/known issues

- The application does not release files it just created for other programs to be allowed to open and edit them. (This might only be happening on Windows, but I'm not sure.) I'm looking into why this is, but for now you can unlock the file by closing IFF You Really See Eurydice. (Sometimes a file may unlock under other circumstances without closing the program, but this appears to be inconsistent.)
- The application always uses an iff file ending, even if the imported file was another file ending like spf. This does not matter in regard to producing a valid file, but I wanted to provide the user with a choice and am currently not doing so. It *appears* that I cannot change the file endings offered to the user to save as without allowing *every* file ending, at least not with the existing file read/write/pick library I'm using. I'm still looking into it, but if you want to leave the iff and spf (or stx if that ever becomes relevant) separate then you'll need to fix the file endings yourself. (If you intend to combine them, you actually have a leg up: Iff Pencil 2's "IFF file with resources" import function (one easy way to combine the two) only allows imports of files that have iff endings.)
- NAME chunk (1.0 iff label format) handling was implemented in a shortcut sort of way. I don't anticipate this being a problem for the vast majority of objects, because most objects only have one NAME chunk, or the NAME with all the info is the last one in the file.  However, if you find you get a file with *several* unexpected "not found" labels, please file an issue for me to look into it. That way, I can determine if this shortcut caused it and how best to implement a new way of handling, if needed.
- This is minor, but I wish the buttons were closer to the look of the game. My testing is telling me this either isn't possible with Compose or I'm not skilled enough to replicate the kinds of shadows it would need to make this look.

### About the author

I'm citrusella, but you may also know me by purplewowies in the Sims 1 community. I've been playing the Sims 1 on and off for years and became interested in prototype porting in 2024 when a thread regarding it was created on Mod the Sims.

At some point while trying to get a multi-tile object working (and failing horribly using the existing methods of using exported sprites and Z-buffers to make a set of SPR2 sprites using T-mog), I got the bright idea to try to pull the raw hexadecimal data portion of the resources themselves out of the 1.0 file and import them to a 2.0 file. After some missteps and investigation... it worked!

Sometime in late 2025, after learning my way around how a 1.0 iff is structured compared to 2.0, I tried my hand at completely porting entire resources one-by-one manually using only a hex editor. After that was successful, I suspected it'd be possible to somehow automate it... and then over the new year into 2026, in a whirling flurry of coding and learning, I (somehow) managed to slap this thing together in less than two weeks!

I coded this in Kotlin Multiplatform because I was already familiar with Kotlin as a programming language (though a lot of the Compose aspects of this project were new to me). This isn't the first application I've coded to some state of completion, but it *is* the first I've managed to release!

Enjoy!

### Acknowledgements

- [FileKit](https://github.com/vinceglb/FileKit) was used to provide the file picking and saving functionality
- [SimsTek Wiki](https://simstek.fandom.com/wiki/IFF) as well as the older [*The Sims*™ Technical Aspects](https://web.archive.org/web/20220410061934/http://simtech.sourceforge.net/tech/iff.html) (dead, Wayback link) were valuable resources for me to gain enough understanding of the underlying format of iff hexadecimal to be able to understand what was needed for conversion
- [TS1 - Maxis Beta Conversions Project (+Sprites!)](https://modthesims.info/showthread.php?t=686236) thread at Mod the Sims, for getting me to finally start successfully object hacking, and because it's where I've discussed some of my discoveries doing this kind of thing, as I started looking more and more into the hex code
- The [FreeSO](https://freeso.org/) Discord server, because I sort of "liveblogged"/rubber ducked a few issues I was having in its Sims 1 channel, trying to figure out why various sprite things (weird layering, crashing with SPR#) were happening so it was a help in me figuring out enough to not give up
- [TheSims.css](https://github.com/inbn/TheSims.css) for providing a reference for how Sims-like styling could be achieved, even though CSS is not Kotlin/Compose

### License

This project is licensed under GNU GPL 3.0.

[Comic Neue](https://fonts.google.com/specimen/Comic+Neue), the Comic-Sans-MS-like font used in the tool's interface, is available at Google Fonts [under the SIL Open Font License, version 1.1](https://fonts.google.com/specimen/Comic+Neue/license).

[FileKit](https://github.com/vinceglb/FileKit), the library used to provide file picking and saving functionality, is published [under the MIT License](https://github.com/vinceglb/FileKit?tab=MIT-1-ov-file).

## Getting this running inside an IDE like IntelliJ IDEA, for developers

This section is largely the provided boilerplate IntelliJ IDEA creates with an empty project.

This is a Kotlin Multiplatform project targeting Desktop (JVM).

[jvmMain](./composeApp/src/jvmMain/kotlin) contains the "meat" of the code for this application.

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…