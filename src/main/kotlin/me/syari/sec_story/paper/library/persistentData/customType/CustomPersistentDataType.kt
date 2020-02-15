package me.syari.sec_story.paper.library.persistentData.customType

import org.bukkit.persistence.PersistentDataType
import java.util.*

object CustomPersistentDataType {
    val UUID: PersistentDataType<ByteArray, UUID> =
        PersistentDataTypeUUID()
}