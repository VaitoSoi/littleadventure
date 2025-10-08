package dev.vaito.screen.custom;

import dev.vaito.items.custom.BackpackItem;
import dev.vaito.screen.ModScreenHandler;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;

import java.util.ArrayList;

// Sorry for copy and paste the wiki, I have no idea WTF is this ;-;
public class BackpackScreenHandler extends ScreenHandler {
    private final PlayerEntity player;
    private final SimpleInventory inventory;
    private final Hand openingHand;

    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, playerInventory.player.getActiveHand());
    }

    public BackpackScreenHandler(
            int syncId, // For syncing between client and server
            PlayerInventory playerInventory, // Who using this backpack, I don't know :D
            Hand openingHand // To get the item
    ) {
        super(ModScreenHandler.BACKPACK_SCREEN_HANDLER, syncId);

        // Assigning stuff
        this.player = playerInventory.player;
        this.openingHand = openingHand;

        // Init the inventory
        this.inventory = new SimpleInventory(BackpackItem.INVENTORY_SIZE) {
            @Override
            // Auto-save when player do something "dirty" >:)
            public void markDirty() {
                super.markDirty();
                if (!player.getWorld().isClient) {
                    ItemStack holding = getHoldingStack();
                    if (!holding.isEmpty()) {
                        // Persist back to the ItemStack’s component
                        var inventory = new java.util.ArrayList<ItemStack>(BackpackItem.INVENTORY_SIZE);
                        for (int i = 0; i < BackpackItem.INVENTORY_SIZE; i++) inventory.add(getStack(i).copy());
                        BackpackItem.saveContainer(holding, inventory);
                    }
                }
            }
        };
        // Don't know what this does :_D
        // inventory.onOpen(playerInventory.player);

        if (!player.getWorld().isClient) {
            // Getting data
            ItemStack holding = getHoldingStack();
            ContainerComponent component = BackpackItem.getAndAppendContainer(holding);

            // Copy the items from the backpack
            ArrayList<ItemStack> temp = new ArrayList<>(BackpackItem.INVENTORY_SIZE);
            component.stream().limit(BackpackItem.INVENTORY_SIZE).forEach(temp::add);

            // Padding emptiness
            while (temp.size() < BackpackItem.INVENTORY_SIZE) temp.add(ItemStack.EMPTY);

            // Put the item in actual inventory
            for (int i = 0; i < temp.size(); i++) inventory.setStack(i, temp.get(i));
        }

        // Wiki stuffs
        // This will place the slot in the correct locations for a 9x3 Grid. The slots exist on both server and client!
        // This will not render the background of the slots however, this is the Screens job
        int m;
        int l;

        // Our inventory
        for (m = 0; m < 3; ++m)
            for (l = 0; l < 9; ++l)
                this.addSlot(new Slot(inventory, l + m * 9, 8 + l * 18, 18 + m * 18));

        addPlayerInventorySlots(playerInventory, 8, 84);
        addPlayerHotbarSlots(playerInventory, 8, 142);
    }

    private ItemStack getHoldingStack() {
        return openingHand == Hand.MAIN_HAND ? player.getMainHandStack() : player.getOffHandStack();
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
