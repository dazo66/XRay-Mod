package com.xray.client;

import com.xray.common.XRay;
import com.xray.common.config.ConfigHandler;
import com.xray.common.reference.OreInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;

public class OresSearch
{
	private static Minecraft mc = Minecraft.getMinecraft();

	public static void add( int oreId, int oreMeta, String name, int[] color ) // Takes a string of id:meta or oreName to add to our search list.
	{
		if( name.equals("") || name.toLowerCase().equals( I18n.format("xray.input.gui").toLowerCase() ) || name.isEmpty() ) {
			mc.ingameGUI.getChatGUI().printChatMessage( new TextComponentString( "[XRay] "+I18n.format("xray.message.missing_input") ));
			return;
		}

		for( OreInfo info : XRay.searchList ) {
			if( info.getId() == oreId && info.getMeta() == oreMeta) {
				mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("[XRay] "+I18n.format("xray.message.already_exists")));
				return;
			}
		}

		XRay.searchList.add( new OreInfo( name, name.replaceAll("\\s+", ""), oreId, oreMeta, color, true ) );
		String notify = "[XRay] "+I18n.format( "xray.message.added_block", name );
		mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(notify));

		ConfigHandler.add(name, oreId, oreMeta, color);
	}

	public static void update( OreInfo original, String name, int[] color ) {
		if( !XRay.searchList.contains( original ) ) {
			// This really shouldn't happen but hay, lets support it anyway.
			mc.ingameGUI.getChatGUI().printChatMessage( new TextComponentString( "[XRay] "+I18n.format("xray.message.unknown") ));
			return;
		}

		OreInfo preserve = new OreInfo( original.getDisplayName(), original.getOreName(), original.getId(), original.getMeta(), original.color, original.draw );

		OreInfo tmpNew = null;
		for ( OreInfo ore : XRay.searchList ) {
			if( ore == original ) {
				ore.displayName = name;
				ore.color = color;
				tmpNew = ore;
				break;
			}
		}

		if( tmpNew != null ) {
			ConfigHandler.updateInfo(preserve, tmpNew);
			String notify = "[XRay] "+I18n.format( "xray.message.added_block", preserve.getOreName() );
			mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(notify));
		} else {
			mc.ingameGUI.getChatGUI().printChatMessage( new TextComponentString( I18n.format("xray.message.unknown") ));
		}
	}

	public static void remove( OreInfo original ) {
		if( !XRay.searchList.contains( original ) ) {
			// This really shouldn't happen but hay, lets support it anyway.
			mc.ingameGUI.getChatGUI().printChatMessage( new TextComponentString( I18n.format("xray.message.unknown") ));
			return;
		}

		XRay.searchList.remove( original );
		ConfigHandler.remove(original);

		String notify = "[XRay] "+I18n.format( "xray.message.added_block", original.getOreName() );
		mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(notify));
	}
}
