package io.github.citrusella.iffyoureallyseeeurydice

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

import iffyoureallyseeeurydice.composeapp.generated.resources.Res
import iffyoureallyseeeurydice.composeapp.generated.resources.explanation
import iffyoureallyseeeurydice.composeapp.generated.resources.input_button
import iffyoureallyseeeurydice.composeapp.generated.resources.input_error
import iffyoureallyseeeurydice.composeapp.generated.resources.input_file
import iffyoureallyseeeurydice.composeapp.generated.resources.input_instruction
import iffyoureallyseeeurydice.composeapp.generated.resources.input_note
import iffyoureallyseeeurydice.composeapp.generated.resources.input_resources
import iffyoureallyseeeurydice.composeapp.generated.resources.input_success
import iffyoureallyseeeurydice.composeapp.generated.resources.no_file
import iffyoureallyseeeurydice.composeapp.generated.resources.output_button
import iffyoureallyseeeurydice.composeapp.generated.resources.output_done
import iffyoureallyseeeurydice.composeapp.generated.resources.output_instruction
import iffyoureallyseeeurydice.composeapp.generated.resources.string_space
import iffyoureallyseeeurydice.composeapp.generated.resources.title_why
import iffyoureallyseeeurydice.composeapp.generated.resources.welcome
import io.github.citrusella.iffyoureallyseeeurydice.theme.Theme
import io.github.citrusella.iffyoureallyseeeurydice.theme.edgePad
import io.github.citrusella.iffyoureallyseeeurydice.theme.paragraphPad
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.downloadDir
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.parent
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.sink
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.launch
import kotlinx.io.buffered
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
fun App() {
    Theme {
        var showComplete by remember { mutableStateOf(false) } // is conversion complete?
        var validation by remember { mutableStateOf("none")} // is valid 1.0?
        var inputPF by remember { mutableStateOf<PlatformFile?>(null) } // input file
        val chunkList by remember { mutableStateOf(mutableListOf<String>()) } // list of iff chunks
        var completeChunks by remember { mutableStateOf(mutableListOf<String>())} // chunk list when complete
        var chunkCounts by remember { mutableStateOf(mapOf<String, Int>()) } // chunk list converted to count list
        var outputFileName by remember { mutableStateOf("none")} // name of converted file plus path
        val coroutineScope = rememberCoroutineScope() // run tasks off the main thread
        //1.0 header for checking against input file to see if it's a 1.0 iff
        val headerValidation = "4946462046494C4520312E303A5459504520464F4C4C4F5745442042592053495A4500204A414D494520444F4F524E424F532026204D41584953203139393600"
        val userDir = FileKit.downloadDir // Current account's download folder is default save location
        var pickerDir: PlatformFile? by remember { mutableStateOf(userDir) } // remember picker's directory in same session
        var saverDir: PlatformFile? by remember { mutableStateOf(userDir) } // remember saver's directory in same session
        val shorterLengthFormat = HexFormat {
            number.removeLeadingZeros = true
            number.minLength = 8 //format hex string so that it is a minimum of eight characters, for converting parts of header
        }
        val byteArray = HexFormat {
            bytes
        } // byte array
        //Supports importing stx even though I haven't seen in prototypes
        val inputFile = rememberFilePickerLauncher(type = FileKitType.File(extensions = listOf("iff", "spf", "stx")),
            mode = FileKitMode.Single,
            title = stringResource(Res.string.input_button),
            directory = pickerDir)  { filePicked ->
            inputPF = filePicked // Sets for use elsewhere
            chunkList.clear() // Clears chunkList if new file chosen so it doesn't just grow
            showComplete = false // Clears completion because new file chosen

            filePicked?.let {
                coroutineScope.launch {
                    BookmarkManager.save(it) // Empty file created after conversion if this is not here
                }
            }
        }
        Box { //needed to have the column and scrollbar work together
            val verticalScroll = rememberScrollState() //remember where the scrollbar is
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .safeContentPadding()
                    .fillMaxSize()
                    .padding(edgePad)
                    .verticalScroll(verticalScroll), // scroll if too big for window, unsure how to implement scrollbar right now but the scrolling itself works
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    buildAnnotatedString {
                        append(stringResource(Res.string.welcome)) // Welcome to IFF You...
                        append(stringResource(Res.string.string_space)) // Space for languages that utilize between-sentence spaces
                        withLink(
                            LinkAnnotation.Url(
                                "https://www.youtube.com/watch?v=OAJIATuky28"
                            )
                        ) {
                            append(stringResource(Res.string.title_why)) // Why that name, links to YouTube video of If You Really See Eurydice
                        }
                        append(stringResource(Res.string.string_space)) // Space so English sentences are properly spaced
                        append(stringResource(Res.string.explanation)) // Short explanation of what this program is good for, longer in readme
                    },
                    style = MaterialTheme.typography.bodyMedium, // font
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.size(paragraphPad))
                Text(
                    stringResource(Res.string.input_instruction),// First you'll need...
                    style = MaterialTheme.typography.bodyMedium, // font
                    color = MaterialTheme.colorScheme.onPrimary
                ) // off-white text
                Button(
                    onClick = { inputFile.launch() },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.defaultMinSize(1.dp, 1.dp)
                ) {
                    Text(
                        stringResource(Res.string.input_button),
                        style = MaterialTheme.typography.bodyMedium
                    ) //Select 1.0 iff
                }
                if (inputPF != null) {
                    pickerDir = inputPF!!.parent()
                    println(pickerDir) //This line exists SOLELY to prevent a lint error from triggering an "are you sure" with EVERY GitHub commit.
                    Text(
                        stringResource(Res.string.input_file, inputPF!!.name),// Selected file: iffName.iff
                        style = MaterialTheme.typography.bodyMedium, // font
                        color = MaterialTheme.colorScheme.onPrimary
                    ) // off-white text
                    coroutineScope.launch {
                        val inputBytes = inputPF!!.readBytes() // Get byte array of entire file
                        val inputHex =
                            inputBytes.toHexString() // Convert to hex string because my existing code was built to work with hex strings lol
                        val inputHeader = inputHex.take(headerValidation.length) // Take header of input file
                        validation = if (inputHeader.equals(headerValidation, ignoreCase = true)) {
                            "success" // if matches 1.0 header
                        } else {
                            "error" // if it doesn't match 1.0 header
                        }
                    }
                } else {
                    Text(
                        stringResource(Res.string.no_file),//No file selected
                        style = MaterialTheme.typography.bodyMedium, // font
                        color = MaterialTheme.colorScheme.onPrimary
                    ) // off-white text
                    Spacer(modifier = Modifier.size(paragraphPad))
                }
                if (inputPF != null && validation == "success") {
                    Text(
                        stringResource(Res.string.input_success),
                        style = MaterialTheme.typography.bodyMedium, // font
                        color = MaterialTheme.colorScheme.onPrimary
                    ) // Looks like a 1.0 iff!
                } else if (inputPF != null && validation == "error") {
                    Text(
                        stringResource(Res.string.input_error),
                        style = MaterialTheme.typography.bodyMedium, // font
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    ) // No it doesn't
                    Spacer(modifier = Modifier.size(paragraphPad))
                }
                if (inputPF != null) {
                    coroutineScope.launch {
                        val inputBytes = inputPF!!.readBytes() // byte array
                        val inputHex = inputBytes.toHexString() // hex string
                        var chunkOffset = 128 // where the data starts, 64 times 2
                        var chunkTypeBytes: String
                        var chunkType: String
                        val chunkList = mutableListOf<String>()
                        var lengthBytes: String
                        var length: Int
                        while (chunkOffset < inputHex.length) {
                            val lengthOffset = chunkOffset + 8 // where length starts in chunk header
                            val lengthEnd = lengthOffset + 8 // where length ends in chunk header
                            chunkTypeBytes =
                                inputHex.substring(chunkOffset, lengthOffset) // chunk identifier, i.e. BHAV, OBJD, etc.
                            chunkType = chunkTypeBytes.hexToByteArray(byteArray)
                                .decodeToString() // string it so it's readable to a user
                            chunkList.add(chunkType) // add to list to present to user
                            lengthBytes = inputHex.substring(
                                lengthOffset,
                                lengthEnd
                            ) // length, needed here to find where next chunk starts
                            length = lengthBytes.hexToInt() // to int in order to find next offset
                            length = length.times(2) //times 2 so that it's the actual number of characters
                            try {
                                chunkOffset += length // get to next offset
                            } catch (_: Exception) {
                                chunkOffset =
                                    inputHex.length // if offset is bigger than entire iff length, exception gets thrown and offset is set to iff length and loop stops running
                            }
                        }
                        completeChunks =
                            chunkList // chunk list kept showing as empty in string, this fixes it, may be unneeded
                        chunkCounts = completeChunks.groupingBy { it }.eachCount()
                    }
                }
                if (inputPF != null && completeChunks.isNotEmpty() && validation != "error") { // if it has chunks and is 1.0 iff
                    Spacer(modifier = Modifier.size(paragraphPad))
                    Text(
                        buildAnnotatedString {
                            append(stringResource(Res.string.input_resources))
                            append(stringResource(Res.string.string_space))
                            append(
                                chunkCounts
                                    .toString()
                                    .replace("{", "")
                                    .replace("}", "")
                            )
                        },
                        style = MaterialTheme.typography.bodyMedium, // font
                        color = MaterialTheme.colorScheme.onPrimary
                    ) // Prefix for list
                    Spacer(modifier = Modifier.size(paragraphPad))
                    Text(
                        stringResource(Res.string.input_note),
                        style = MaterialTheme.typography.bodyMedium, // font
                        color = MaterialTheme.colorScheme.onPrimary
                    ) // note about NAME and XXXX
                }
                if (inputPF != null && validation == "success") {
                    Spacer(modifier = Modifier.size(paragraphPad))
                    Text(
                        stringResource(Res.string.output_instruction),
                        style = MaterialTheme.typography.bodyMedium, // font
                        color = MaterialTheme.colorScheme.onPrimary
                    ) // Everything look good?
                }
                val outputFile = rememberFileSaverLauncher { file ->
                    if (file != null) {
                        if (inputPF != null) {
                            coroutineScope.launch {
                                val inputBytes = inputPF!!.readBytes() //bytes
                                val inputHex = inputBytes.toHexString() //hex
                                //2.0 iff header, 2.0 version chosen so that I don't have to worry about rsmp even a little
                                val outputHeader: ByteArray =
                                    "4946462046494C4520322E303A5459504520464F4C4C4F5745442042592053495A4500204A414D494520444F4F524E424F532026204D41584953203139393600".hexToByteArray()
                                file.write(outputHeader) //write header before even trying anything else
                                var chunkOffset = 128 //post-header offset
                                var chunkTypeBytes: String
                                var chunkType: String
                                var lengthBytes: String
                                var length: Int
                                var inputChunk: String
                                var dataLength: Int
                                var nameChunk =
                                    "4e414d45" //beginning of NAME chunk so that when it gets stapled to the sub-chunk it's a whole chunk
                                val nameChunkInfo = inputHex.substringAfterLast(
                                    nameChunk,
                                    "00000000"
                                ) //get everything after last NAME in file, but if no name in file then throw a bunch of zeros
                                val nameLength = if (nameChunkInfo != "00000000") {
                                    inputHex.substringAfterLast(nameChunk).take(8) //if it got data, get name length
                                } else {
                                    "00000010" //if it didn't get data, pretend the NAME is only a header with no data
                                }
                                var nameLengthInt = nameLength.hexToInt().times(2) //get chunk's length as int
                                nameLengthInt -= 8 //subtract 8 so that it only takes to end of block when accounting for nameChunk
                                println("$nameChunkInfo $nameLength $nameLengthInt")
                                nameChunk += inputHex.substringAfterLast(nameChunk)
                                    .take(nameLengthInt) //add the rest of the chunk to NAME to make full chunk
                                println("Full name $nameChunk")
                                while (chunkOffset < inputHex.length) { // only keep going until end of file
                                    val outputSink = file.sink(append = true)
                                        .buffered() //allow for appending chunks to existing file with header
                                    val lengthOffset = chunkOffset + 8 //length offset
                                    val lengthEnd = lengthOffset + 8 //end of length
                                    chunkTypeBytes = inputHex.substring(
                                        chunkOffset,
                                        lengthOffset
                                    ) //get chunk type (i.e. BHAV, OBJD, DGRP, etc.)
                                    chunkType = chunkTypeBytes.hexToByteArray(byteArray)
                                        .decodeToString() // chunk type as string, used only to exclude NAME from conversion
                                    lengthBytes =
                                        inputHex.substring(lengthOffset, lengthEnd) // length in bytes from chunk header
                                    length = lengthBytes.hexToInt() // length as int
                                    length = length.times(2) // multiply by 2 for string length
                                    dataLength = length.div(2) - 16 // subtract header length from data length
                                    inputChunk = try {
                                        inputHex.substring(chunkOffset, chunkOffset + length) // get to next chunk
                                    } catch (_: Exception) {
                                        inputHex.substring(chunkOffset) // assume last chunk if try block throws exception
                                    }
                                    println(chunkOffset)
                                    val outputType = inputChunk.take(8) // type for output
                                    val outputSize = inputChunk.substring(8, 16) // size for output
                                    var outputSizeInt = outputSize.hexToInt() // size as int
                                    outputSizeInt += 60 // add 60 because 2.0 header is 60 longer than 1.0 (16 + 60 = 76)
                                    val outputSizeHex = outputSizeInt.toHexString(shorterLengthFormat) // back to hex
                                    val outputId = inputChunk.substring(16, 20) // ID number (i.e. 4096, 301, 0, etc.)
                                    val outputFlags = inputChunk.substring(20, 24) //flags (usually 0000 or 0010)
                                    // empty label if nameId is FFFFFFFF
                                    val emptyLabel =
                                        "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                                    // error label if nameId isn't in NAME chunk used for label decoding
                                    val nameError =
                                        "6C6162656C206E6F7420666F756E6420696E204E414D45206368756E6B206F72206E6F204E414D45206368756E6B20666F756E64000000000000000000000000"
                                    val outputNameId = inputChunk.substring(24, 32) // ID for label
                                    var outputLabelPrefix = ""
                                    var labelBytes = ""
                                    var chunkName = ""
                                    if (outputNameId.equals("FFFFFFFF", ignoreCase = true)) {
                                        labelBytes = emptyLabel // empty label is empty
                                    } else if (outputNameId != "FFFFFFFF") { //if label is not blank (nameId FFFFFFFF is blank label)
                                        //var outputNameIdBytes = outputNameId.toByteArray()
                                        var outputNameIdFlipped = outputNameId.substring(
                                            6,
                                            8
                                        ) // start flipping the nameId to the other endianness
                                        outputNameIdFlipped += outputNameId.substring(4, 6)
                                        outputNameIdFlipped += outputNameId.substring(2, 4)
                                        outputNameIdFlipped += outputNameId.take(2) // finish flipping nameId
                                        val nameChunkData =
                                            nameChunk.substring(32) // get NAME's header out of way for substring search so it doesn't affect what's found
                                        val magicWord = nameChunkData.substring(16, 24)
                                        if (magicWord.equals("454d414e", ignoreCase = true)) { // if NAME entries
                                            println("Handling for null terminated NAME chunks has not been implemented yet")
                                            labelBytes =
                                                "48616E646C696E6720666F72206E756C6C2D7465726D696E61746564206E616D65206368756E6B73206973206E6F7420696D706C656D656E7465642079657400"
                                            val nameChunkDataTrimmed = nameChunkData.substring(32)
                                            chunkName = nameChunkDataTrimmed.substringAfter(outputNameIdFlipped)
                                            labelBytes = chunkName.substringBefore("00")
                                            if (labelBytes.length % 2 != 0) labelBytes += "0"
                                        } else {
                                            val outputLabelLength = nameChunkData.substringAfter(outputNameIdFlipped)
                                                .take(2) // get label length for length prefixed NAME entry
                                            var outputLabelLengthInt =
                                                outputLabelLength.hexToInt() // label length as int
                                            outputLabelLengthInt =
                                                outputLabelLengthInt.times(2) // times 2 for string length
                                            outputLabelPrefix =
                                                outputNameIdFlipped + outputLabelLength // prefix to find substring is flipped nameId followed by length prefix
                                            chunkName = nameChunk.substringAfterLast(
                                                outputLabelPrefix,
                                                missingDelimiterValue = nameError
                                            ) // get label, or provide error label if nameId not found
                                            labelBytes = if (chunkName != nameError) {
                                                chunkName.take(outputLabelLengthInt) // if label found, set labelBytes to label
                                            } else {
                                                chunkName // if label errored, leave as chunkName
                                            }
                                        }
                                    }
                                    println("$outputNameId $outputLabelPrefix")

                                    //var label = chunkName
                                    //while (label.length < 64) label += "\u0000"
                                    //var labelBytes = label.encodeToByteArray().toHexString(byteArray)
                                    while (labelBytes.length < 128) labelBytes += "0" //if the label isn't 64 characters, pad with null
                                    if (labelBytes.length > 128) { //if the label is MORE than 64 characters, replace with error label because it's probably wrong
                                        labelBytes = nameError
                                    }
                                    val outputData = inputChunk.substring(32) // chunk data for output
                                    println("${outputType.length}, ${outputSizeHex.length}, ${outputId.length}, ${outputFlags.length}, ${labelBytes.length}, ${outputData.length}")
                                    val outputChunk =
                                        "$outputType$outputSizeHex$outputId$outputFlags$labelBytes$outputData" // now put it all together and what do you got?
                                    try {
                                        chunkOffset += length //set to next chunk offset
                                    } catch (_: Exception) {
                                        chunkOffset =
                                            inputHex.length //if chunk offset too big and throws error then set to iff length and exit loop
                                    }
                                    println("$chunkType $length, $dataLength")
                                    println(inputChunk)
                                    val chunkBytes =
                                        outputChunk.hexToByteArray() //make hex string a byte array for writing
                                    if (chunkType != "NAME") {
                                        outputSink.use { bufferedSink ->
                                            bufferedSink.write(chunkBytes)
                                        }
                                    }
                                }
                                showComplete = true // allows successful creation message to show
                                outputFileName = file.path //provides file path for message
                                saverDir = file.parent()
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        outputFile.launch(
                            suggestedName = "converted prototype file",
                            extension = "iff", //TODO: Figure out if FileKit can allow for multiple extension choices but still limited
                            directory = saverDir
                        )
                    },
                    enabled = inputPF != null && validation == "success",//Only work if input file is a 1.0 iff
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.defaultMinSize(1.dp, 1.dp)
                ) {
                    Text(
                        stringResource(Res.string.output_button), //Pick file and save
                        style = MaterialTheme.typography.bodyMedium
                    ) // font
                }
                if (showComplete) {
                    Text(
                        stringResource(Res.string.output_done, outputFileName),
                        style = MaterialTheme.typography.bodyMedium, // font
                        color = MaterialTheme.colorScheme.onPrimary
                    ) //Conversion done!
                    /*Button(onClick = {
                     inputPF = null
                     outputFileName = "none"
                     showComplete = false
                                  },
                    enabled = inputPF != null && validation == "success",//Only work if input file is a 1.0 iff
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiary),
                    modifier = Modifier.defaultMinSize(1.dp,1.dp)
                ) {
                    Text(stringResource(Res.string.restart), //Pick file and save
                        style = MaterialTheme.typography.bodyMedium) // font
                }*/
                }
            }
            VerticalScrollbar(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(), //scrollbar
                adapter = rememberScrollbarAdapter(verticalScroll)
            )
        }
    }
}