package me.lukas81298.flexmc.entity.metadata;

import com.evilco.mc.nbt.tag.TagCompound;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lukas81298.flexmc.Flex;
import me.lukas81298.flexmc.inventory.ItemStack;
import me.lukas81298.flexmc.io.message.Message;
import me.lukas81298.flexmc.util.EnumDirection;
import me.lukas81298.flexmc.util.Vector3f;
import me.lukas81298.flexmc.util.Vector3i;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * @author lukas
 * @since 07.08.2017
 */
@RequiredArgsConstructor
@Getter
public enum MetaDataType {
    BYTE( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            buf.writeByte( ((Number) o).byteValue() );
        }
    } ),
    VAR_INT( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            Message.writeVarInt( ((Number)o).intValue(), buf );
        }
    } ),
    FLOAT( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            buf.writeFloat( ((Number)o).floatValue() );
        }
    } ),
    STRING( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            Message.writeString( (String) o, buf );
        }
    } ),
    CHAT_COMPONENT( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            Message.writeString( Flex.getGson().toJson( o ), buf );
        }
    } ),
    SLOT( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            ItemStack itemStack = (ItemStack) o;
            itemStack.serialize( buf );
        }
    } ),
    BOOLEAN( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            buf.writeBoolean( o.equals( Boolean.TRUE ) );
        }
    } ),
    ROTATION( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            Vector3f vector3f = (Vector3f) o;
            buf.writeFloat( vector3f.getX() );
            buf.writeFloat( vector3f.getY() );
            buf.writeFloat( vector3f.getZ() );
        }
    } ),
    POSITION( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            Vector3i vector3i = (Vector3i) o;
            buf.writeLong( vector3i.asLong() );
        }
    } ),
    POSITION_OPTIONAL( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            buf.writeBoolean( o != null );
            if( o != null ) {
                Vector3i vector3i = (Vector3i) o;
                buf.writeLong( vector3i.asLong() );
            }
        }
    } ),
    DIRECTION( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            EnumDirection enumDirection = (EnumDirection) o;
            Message.writeVarInt( enumDirection.ordinal(), buf );
        }
    } ),
    UUID_OPTIONAL( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            buf.writeBoolean( o != null );
            if( o != null ) {
                Message.writeUUID( (UUID) o, buf );
            }
        }
    } ),
    NBT_TAG( new BiConsumer<ByteBuf, Object>() {
        @Override
        public void accept( ByteBuf buf, Object o ) {
            TagCompound tagCompound = (TagCompound) o;
            Message.writeNbtTag( tagCompound, buf );
        }
    } );

    private final BiConsumer<ByteBuf, Object> writer;

}
