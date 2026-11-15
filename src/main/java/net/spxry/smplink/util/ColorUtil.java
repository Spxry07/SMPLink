package net.spxry.smplink.util;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {

    private static final Pattern HEX_AMP = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern HEX_HASH = Pattern.compile("(?<!&)#([A-Fa-f0-9]{6})");

    private ColorUtil() {}

    public static String colorize(String text) {
        if (text == null) return "";
        text = hexToSection(HEX_AMP, text);
        text = hexToSection(HEX_HASH, text);
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private static String hexToSection(Pattern p, String text) {
        var m = p.matcher(text);
        var sb = new StringBuffer();
        while (m.find()) {
            var sb2 = new StringBuilder("§x");
            for (char c : m.group(1).toCharArray()) sb2.append('§').append(c);
            m.appendReplacement(sb, Matcher.quoteReplacement(sb2.toString()));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
