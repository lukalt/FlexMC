package me.lukas81298.flexmc.io.message.play;

import me.lukas81298.flexmc.io.message.MessageRegistry;
import me.lukas81298.flexmc.io.message.play.client.*;
import me.lukas81298.flexmc.io.message.play.server.*;

/**
 * @author lukas
 * @since 04.08.2017
 */
public class MessageRegistryPlay extends MessageRegistry {

    @Override
    public void register() {

        registerServerMessage( 0x00, MessageS00SpawnObject.class );
        registerServerMessage( 0x04, MessageS04SpawnPainting.class );
        registerServerMessage( 0x05, MessageS05SpawnPlayer.class );
        registerServerMessage( 0x06, MessageS06Animation.class );
        registerServerMessage( 0x08, MessageS08BlockBreakAnimation.class );
        registerServerMessage( 0x0B, MessageS0BBlockChange.class );
        registerServerMessage( 0x0E, MessageS0ETabComeplete.class );
        registerServerMessage( 0x0F, MessageS0FChatMessage.class );
        registerServerMessage( 0x16, MessageS16SetSlot.class );
        registerServerMessage( 0x18, MessageS18PluginMessage.class );
        registerServerMessage( 0x1A, MessageS1ADisconnect.class );
        registerServerMessage( 0x1E, MessageS1EChangeGameState.class );
        registerServerMessage( 0x1F, MessageS1FKeepAlive.class );
        registerServerMessage( 0x20, MessageS20ChunkData.class );
        registerServerMessage( 0x23, MessageS23JoinGame.class );
        registerServerMessage( 0x26, MessageS26EntityRelMove.class );
        registerServerMessage( 0x27, MessageS27EntityRelMoveLook.class );
        registerServerMessage( 0x28, MessageS28EntityLook.class );
        registerServerMessage( 0x2C, MessageS2CPlayerAbilities.class );
        registerServerMessage( 0x2E, MessageS2EPlayerList.class );
        registerServerMessage( 0x2F, MessageS2FPlayerPositionAndLook.class );
        registerServerMessage( 0x32, MessageS32DestroyEntities.class );
        registerServerMessage( 0x33, MessageS33RemoveEntityEffect.class );
        registerServerMessage( 0x34, MessageS34ResourcePackSend.class );
        registerServerMessage( 0x35, MessageS35Respawn.class );
        registerServerMessage( 0x36, MessageS36EntityHeadLook.class );
        registerServerMessage( 0x3C, MessageS3CEntityMetaData.class );
        registerServerMessage( 0x41, MessageS41UpdateHealth.class );
        registerServerMessage( 0x46, MessageS46SpawnPosition.class );
        registerServerMessage( 0x47, MessageS47TimeUpdate.class );
        registerServerMessage( 0x4B, MessageS4BCollectItem.class );
        registerServerMessage( 0x4C, MessageS4CEntityTeleport.class );

        registerClientMessage( 0x00, MessageC00TeleportConfirm.class );
        registerClientMessage( 0x01, MessageC01TabComplete.class );
        registerClientMessage( 0x02, MessageC02ChatMessage.class );
        registerClientMessage( 0x03, MessageC03ClientStatus.class );
        registerClientMessage( 0x04, MessageC04ClientSettings.class );
        registerClientMessage( 0x05, MessageC05ConfirmTransaction.class );
        registerClientMessage( 0x06, MessageC06EnchantItem.class );

        registerServerMessage( 0x08, MessageC08CloseWindow.class );
        registerClientMessage( 0x09, MessageC09PluginMessage.class );
        registerClientMessage( 0x0B, MessageC0BKeepAlive.class );
        registerClientMessage( 0x0C, MessageC0CPlayer.class );
        registerClientMessage( 0x0D, MessageC0DPlayerPosition.class );
        registerClientMessage( 0x0E, MessageC0EPPlayerPosAndLook.class );
        registerClientMessage( 0x0F, MessageC0FPlayerLook.class );
        registerServerMessage( 0x13, MessageC13PlayerAbilities.class );
        registerClientMessage( 0x14, MessageC14PlayerDigging.class );
        registerClientMessage( 0x15, MessageC15EntityAction.class );

        registerClientMessage( 0x1A, MessageC1AHeldItemChange.class );
        registerClientMessage( 0x1D, MessageC1DAnimation.class );
    }
}
