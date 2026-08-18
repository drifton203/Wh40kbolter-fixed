package com.wh40kbolter.boltermod.gui;

import com.wh40kbolter.boltermod.BolterMod;
import com.wh40kbolter.boltermod.container.ContainerArmoury;
import com.wh40kbolter.boltermod.crafting.ArmouryRecipeRegistry;
import com.wh40kbolter.boltermod.network.PacketArmouryAction;
import com.wh40kbolter.boltermod.network.PacketHandler;
import com.wh40kbolter.boltermod.tileentity.TileEntityArmoury;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GuiArmoury extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(BolterMod.MODID, "textures/gui/armoury.png");

    private static final int BTN_TAB_CRAFT = 1000;
    private static final int BTN_TAB_STORAGE = 1001;

    private final TileEntityArmoury te;
    private final EntityPlayer player;
    private final Map<Integer, GuiButton> buttonMap = new HashMap<>();

    public GuiArmoury(ContainerArmoury container, TileEntityArmoury te, EntityPlayer player) {
        super(container);
        this.te = te;
        this.player = player;
        this.xSize = 176;
        this.ySize = 222;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonMap.clear();

        registerButton(new GuiButton(BTN_TAB_CRAFT, guiLeft + 8, guiTop - 20, 80, 20, "Fabricar"));
        registerButton(new GuiButton(BTN_TAB_STORAGE, guiLeft + 88, guiTop - 20, 80, 20, "Almacen"));

        int startX = guiLeft + xSize + 5;
        int startY = guiTop + 10;
        for (int i = 0; i < ArmouryRecipeRegistry.RECIPES.size(); i++) {
            int col = i / 12;
            int row = i % 12;
            String name = ArmouryRecipeRegistry.RECIPES.get(i).name;
            registerButton(new GuiButton(i, startX + col * 110, startY + row * 18, 100, 18, name));
        }

        updateVisibility();
    }

    private void registerButton(GuiButton button) {
        addButton(button);
        buttonMap.put(button.id, button);
    }

    private void updateVisibility() {
        boolean isCraft = te.getActiveTab() == 0;
        boolean isStorage = te.getActiveTab() == 1;

        setVisible(BTN_TAB_CRAFT, true);
        setVisible(BTN_TAB_STORAGE, true);

        for (int i = 0; i < ArmouryRecipeRegistry.RECIPES.size(); i++) {
            setVisible(i, isCraft);
        }
    }

    private void setVisible(int id, boolean visible) {
        GuiButton btn = buttonMap.get(id);
        if (btn != null) btn.visible = visible;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == BTN_TAB_CRAFT) {
            PacketHandler.CHANNEL.sendToServer(new PacketArmouryAction(PacketArmouryAction.ACTION_SET_TAB, 0, te.getPos()));
        } else if (button.id == BTN_TAB_STORAGE) {
            PacketHandler.CHANNEL.sendToServer(new PacketArmouryAction(PacketArmouryAction.ACTION_SET_TAB, 1, te.getPos()));
        } else if (button.id >= 0 && button.id < ArmouryRecipeRegistry.RECIPES.size()) {
            PacketHandler.CHANNEL.sendToServer(new PacketArmouryAction(PacketArmouryAction.ACTION_CRAFT_RECIPE, button.id, te.getPos()));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = te.getActiveTab() == 0 ? "Armoury - Fabricar" : "Armoury - Almacen";
        fontRenderer.drawString(title, 8, 6, 0x404040);
    }
}