package me.syari.sec_story.paper.library.persistentData.customType

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

class PersistentDataTypeBoolean: PersistentDataType<Byte, Boolean> {
    override fun getPrimitiveType(): Class<Byte> {
        return Byte::class.java
    }

    override fun getComplexType(): Class<Boolean> {
        return Boolean::class.java
    }

    override fun toPrimitive(complex: Boolean, context: PersistentDataAdapterContext): Byte {
        return if(complex) 1 else 0
    }

    override fun fromPrimitive(primitive: Byte, context: PersistentDataAdapterContext): Boolean {
        return primitive == 1.toByte()
    }
}