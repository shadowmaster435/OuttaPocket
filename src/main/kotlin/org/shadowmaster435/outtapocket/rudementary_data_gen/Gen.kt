package org.shadowmaster435.outtapocket.rudementary_data_gen

import org.shadowmaster435.outtapocket.Outtapocket
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter


val sixSided = arrayOf(
    "six_sided_piston",
    "bugged_six_sided_piston",
    "six_sided_sticky_piston",
    "six_sided_hay_block",
    "bugged_six_sided_hay_block"
)
val basicLootTable = "" +
        "{\n" +
        "  \"type\": \"minecraft:block\",\n" +
        "  \"pools\": [\n" +
        "    {\n" +
        "      \"rolls\": 1,\n" +
        "      \"entries\": [\n" +
        "        {\n" +
        "          \"type\": \"minecraft:item\",\n" +
        "          \"name\": \"outta_pocket:?\"\n" +
        "        }\n" +
        "      ],\n" +
        "      \"conditions\": [\n" +
        "        {\n" +
        "          \"condition\": \"minecraft:survives_explosion\"\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  ]\n" +
        "}"
val basicBlockModel = "" +
        "{\n" +
        "  \"parent\": \"minecraft:block/cube_all\",\n" +
        "  \"textures\": {\n" +
        "    \"all\": \"minecraft:block/\"\n" +
        "  }\n" +
        "}"
val basicBlockItemModel = "" +
        "{\n" +
        "  \"parent\": \"outta_pocket:block/?\"\n" +
        "}"
val basicItem = "" +
        "{\n" +
        "  \"model\": {\n" +
        "    \"type\": \"minecraft:model\",\n" +
        "    \"model\": \"outta_pocket:item/?\"\n" +
        "  }\n" +
        "}"
val basicState = "" +
        "{\n" +
        "  \"variants\": {\n" +
        "    \"\": {\n" +
        "      \"model\": \"outta_pocket:block/?\"\n" +
        "    }\n" +
        "  }\n" +
        "}"

fun main() {
    for (str in sixSided) {
        val newStr = str.replace("six_sided_", "").replace("bugged_", "")
//        blockModel(str)
        item(str)
//        itemBlockModel(str)
//        lootTable(str)
//        itemLang(str)
//        blockstate(str)
    }
}

fun blockstate(string: String) {
    write(basicState.replace("?", string), "assets\\outta_pocket\\blockstates", string, "json")
}
fun itemLang(string: String) {
    println("\"item." + Outtapocket.MOD_ID + "." + string + "\": \"\",")
}
fun item(string: String) {
    write(basicItem.replace("?", string), "assets\\outta_pocket\\items", string, "json")
}
fun itemBlockModel(string: String) {
    write(basicBlockItemModel.replace("?", string), "assets\\outta_pocket\\models\\item", string, "json")
}
fun blockModel(string: String) {
    write(basicBlockModel, "assets\\outta_pocket\\models\\block", string, "json")
}

fun lootTable(string: String) {
    write(basicLootTable.replace("?", string), "data\\outta_pocket\\loot_table\\blocks", string, "json")
}

fun write(data: String, path: String, name: String, suffix: String) {
    val sourcePath = "C:\\Users\\shado\\IdeaProjects\\OuttaPocket\\src\\main\\resources"
    val file = File("$sourcePath\\$path\\$name.$suffix")
    if (!file.exists()) {
        file.createNewFile()
    }
    val writer = BufferedWriter(file.writer())
    writer.write(data)
    writer.close()
}


