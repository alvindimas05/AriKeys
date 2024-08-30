package org.alvindimas05.mythickeys.screen;

import com.google.common.collect.ImmutableList;
import org.alvindimas05.mythickeys.MythicKey;
import org.alvindimas05.mythickeys.MythicKeys;
import org.alvindimas05.mythickeys.util.MythicKeysIO;
import org.alvindimas05.mythickeys.util.ModifierKey;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MythicKeyControlsListWidget extends ElementListWidget<MythicKeyControlsListWidget.Entry> {
	final MythicKeysOptions parent;
	int maxKeyNameLength;

	public MythicKeyControlsListWidget(MythicKeysOptions parent, MinecraftClient client) {
		// MinecraftClient client, int width, int height, int y, int itemHeight
		super(client, parent.width + 45, parent.height, 43, 30);
		this.parent = parent;
		String category = null;

		for (MythicKey mythicKey : MythicKeys.getCategorySortedKeybinds()) {
			String keyCat = mythicKey.getCategory();
			if (!keyCat.equals(category)) {
				category = keyCat;
				this.addEntry(new CategoryEntry(Text.literal(keyCat)));
			}

			Text text = Text.literal(mythicKey.getName());
			int i = client.textRenderer.getWidth(text);
			if (i > this.maxKeyNameLength) {
				this.maxKeyNameLength = i;
			}

			this.addEntry(new KeyBindingEntry(mythicKey, text));
		}

	}
//
//	protected int getScrollbarPositionX() {
//		return super.getScrollbarPositionX() + 15;
//	}

	public int getRowWidth() {
		return super.getRowWidth() + 32;
	}

	public class CategoryEntry extends Entry {
		final Text text;
		private final int textWidth;

		public CategoryEntry(Text text) {
			this.text = text;
			this.textWidth = MythicKeyControlsListWidget.this.client.textRenderer.getWidth(this.text);
		}

		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			assert client.currentScreen != null;
			int width = (client.currentScreen.width / 2 - this.textWidth / 2);
			int height = y + entryHeight;
			context.drawText(client.textRenderer, this.text, width, height - 20, 16777215, false);
		}

		public List<? extends Element> children() {
			return Collections.emptyList();
		}

		public List<? extends Selectable> selectableChildren() {
			return ImmutableList.of(new Selectable() {
				public SelectionType getType() {
					return SelectionType.HOVERED;
				}

				public void appendNarrations(NarrationMessageBuilder builder) {
					builder.put(NarrationPart.TITLE, CategoryEntry.this.text);
				}
			});
		}
	}

	public class KeyBindingEntry extends Entry {
		private final MythicKey mythicKey;
		private final Text bindingName;
		private final ButtonWidget editButton;
		private final ButtonWidget resetButton;

		KeyBindingEntry(MythicKey mythicKey, Text bindingName) {
			this.mythicKey = mythicKey;
			this.bindingName = bindingName;

			this.editButton = ButtonWidget.builder(bindingName, (button) -> MythicKeyControlsListWidget.this.parent.focusedMKey = mythicKey)
					.dimensions(0, 0, 135, 20).narrationSupplier(
							supplier -> mythicKey.isUnbound() ? Text.translatable("narrator.controls.unbound", bindingName) : Text.translatable(
									"narrator.controls.bound", bindingName, supplier.get())).build();

			this.resetButton = ButtonWidget.builder(Text.translatable("controls.reset"), (button) -> {
				mythicKey.setBoundKey(mythicKey.getKeyCode(), false);
				mythicKey.resetBoundModifiers();
				MythicKeysIO.save();
				KeyBinding.updateKeysByCode();
			}).dimensions(0, 0, 50, 20).narrationSupplier(supplier -> Text.translatable("narrator.controls.reset", bindingName)).build();
		}

		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			boolean bl = parent.focusedMKey == this.mythicKey;
			int width = x + 20 - maxKeyNameLength;
			int height = y + entryHeight / 2;
			context.drawText(client.textRenderer, this.bindingName, width, height - 9 / 2, 16777215, false);

			this.editButton.setX(x + 65);
			this.editButton.setY(y);
			MutableText editMessage = Text.empty();

			if(Objects.equals(this.mythicKey.getType().getPath(), "key")) {
				this.resetButton.setX(x + 210);
				this.resetButton.setY(y);
				this.resetButton.active = this.mythicKey.hasChanged();
				this.resetButton.render(context, mouseX, mouseY, tickDelta);

				for (ModifierKey modifier : this.mythicKey.getBoundModifiers()) {
					editMessage.append(Text.translatable(modifier.getTranslationKey()));
					editMessage.append(Text.literal(" + "));
				}
				editMessage.append(this.mythicKey.getBoundKeyCode().getLocalizedText().copyContentOnly());
				editMessage = editMessage.copy();

				boolean bl2 = false;
				if (!this.mythicKey.isUnbound()) {
					final List<KeyBinding> bindings = new ArrayList<>(List.of(client.options.allKeys));
					for (KeyBinding keyBinding : bindings) {
						if (keyBinding.getBoundKeyTranslationKey().equals(mythicKey.getBoundKeyCode().getTranslationKey()) && mythicKey.getBoundModifiers()
								.size() == 0) {
							bl2 = true;
							break;
						}
					}
					for (MythicKey key : MythicKeys.getKeybinds()) {
						if(Objects.equals(key.getType().getPath(), "voice")) continue;
						if (!key.equals(mythicKey) && key.getBoundKeyCode().equals(mythicKey.getBoundKeyCode())) {
							if (key.testModifiers(mythicKey.getBoundModifiers())) {
								bl2 = true;
								break;
							}
						}
					}
				}

				if (bl) {
					this.editButton.setMessage((Text.literal("> ")).append(editMessage.formatted(Formatting.YELLOW)).append(" <").formatted(Formatting.YELLOW));
				} else if (bl2) {
					this.editButton.setMessage(editMessage.formatted(Formatting.RED));
				} else this.editButton.setMessage(editMessage);
			} else {
				editMessage = Text.literal(this.mythicKey.getVoice());
				this.editButton.setMessage(editMessage);
				this.editButton.active = false;
			}

			this.editButton.render(context, mouseX, mouseY, tickDelta);
		}

		public List<? extends Element> children() {
			return ImmutableList.of(this.editButton, this.resetButton);
		}

		public List<? extends Selectable> selectableChildren() {
			return ImmutableList.of(this.editButton, this.resetButton);
		}

		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.editButton.mouseClicked(mouseX, mouseY, button)) {
				return true;
			} else {
				return this.resetButton.mouseClicked(mouseX, mouseY, button);
			}
		}

		public boolean mouseReleased(double mouseX, double mouseY, int button) {
			return this.editButton.mouseReleased(mouseX, mouseY, button) || this.resetButton.mouseReleased(mouseX, mouseY, button);
		}
	}

	public abstract static class Entry extends ElementListWidget.Entry<Entry> {
	}
}
