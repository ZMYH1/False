package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "Capes", description = "Capes", category = ModuleCategory.RENDER)
class Capes :Module(){
    val mode = ListValue("Capes", arrayOf("Astolfo","Christmas","Cobalt","Dark","Distance","Envy",
    "ETB","FDP","Flux","Funny","Hypixel","Lunar","minecon_2011","minecon_2012","minecon_2013","minecon_2015",
    "minecon_2016","mojang","mojang_classic","mojira","Moon","new_year","Novoline","PowerX","prismarine",
    "realms","Rise","scrolls","snowman","spade","star","Sunny","Target","translator","turtle","VapeV4"),"Novoline")

    fun path(): String{
        return "liquidbounce/capes/" + mode.get() + ".png"
    }
}