/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.cct.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.thedragonteam.cct.api.crafting.cct_2x2.SlotCrafting;
import net.thedragonteam.cct.api.crafting.cct_2x2.TwoByTwoManager;
import net.thedragonteam.cct.container.base.ContainerTableBase;
import net.thedragonteam.cct.container.base.InventoryCraftingImproved;
import net.thedragonteam.cct.tileentity.TileEntityTwoByTwo;

public class ContainerTwoByTwo extends ContainerTableBase {

    private static final int ITEM_BOX = 18;
    private static final int RECIPE_SLOTS = 5;
    private static final int RECIPE_SIZE = 2;
    private static final int RECIPE_SIZE_TOTAL = 4;
    private static final int ROW_SLOTS = 9;
    private static final int FULL_INVENTORY_SLOTS = RECIPE_SLOTS + 36;
    private static final int MAIN_INVENTORY_SLOTS = RECIPE_SLOTS + 27;
    private final World world;
    public InventoryCraftingImproved craftMatrix = new InventoryCraftingImproved(this, 2, 2);
    public IInventory craftResult = new InventoryCraftResult();

    public ContainerTwoByTwo(InventoryPlayer playerInventory, TileEntityTwoByTwo tile) {
        super(tile, RECIPE_SLOTS, MAIN_INVENTORY_SLOTS, FULL_INVENTORY_SLOTS);
        this.world = tile.getWorld();
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 107, 35));

        for (int i = 0; i < RECIPE_SIZE; ++i)
            for (int j = 0; j < RECIPE_SIZE; ++j)
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * RECIPE_SIZE, 31 + j * ITEM_BOX, 25 + i * ITEM_BOX));

        for (int k = 0; k < 3; ++k)
            for (int i1 = 0; i1 < ROW_SLOTS; ++i1)
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * ITEM_BOX, 84 + k * ITEM_BOX));

        for (int l = 0; l < ROW_SLOTS; ++l)
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * ITEM_BOX, 142));

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.craftResult.setInventorySlotContents(0, TwoByTwoManager.getInstance().findMatchingRecipe(this.craftMatrix, this.world));
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        onContainerClosed(playerIn, this.world.isRemote, RECIPE_SIZE_TOTAL, this.craftMatrix);
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
     * is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}