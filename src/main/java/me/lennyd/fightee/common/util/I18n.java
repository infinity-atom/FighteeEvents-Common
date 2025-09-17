package me.lennyd.fightee.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lennyd.fightee.common.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18n {
    private static JsonObject translations;

    public static void setup() {
        try (InputStream in = Main.PLUGIN.getResource("en_au.json")) {
            if (in == null) {
                throw new IllegalStateException("Resource not found: en_au.json");
            }

            try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                translations = JsonParser.parseReader(reader).getAsJsonObject();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load translations", e);
        }
    }

    public static String get(String key, Object... args) {
        // Traverse the JSON structure
        String[] parts = key.split("\\.");
        JsonElement current = translations;

        for (String part : parts) {
            if (current != null && current.isJsonObject()) {
                current = current.getAsJsonObject().get(part);
            } else {
                current = null;
                break;
            }
        }

        // Fallback to key if missing or not a primitive string
        if (current == null || !current.isJsonPrimitive() || !current.getAsJsonPrimitive().isString()) {
            return key;
        }

        String value = current.getAsString();

        // Argument formatting: Replace {0}, {1}, etc.
        if (args.length > 0) {
            value = formatArgs(value, args);
        }

        value = applyColors(value);

        return value;
    }

    // Replace {0}, {1}, ... with arguments
    private static final Pattern ARG_PATTERN = Pattern.compile("\\{(\\d+)\\}");

    private static String formatArgs(String message, Object... args) {
        Matcher matcher = ARG_PATTERN.matcher(message);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1));
            String replacement = (index < args.length && args[index] != null) ? args[index].toString() : matcher.group(0);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static Component getComponent(String key, Object... args) {
        String msg = get(key, args);
        if (msg == null || msg.isEmpty()) {
            msg = key;
        }

        return MiniMessage.miniMessage().deserialize(msg);
    }

    private static final Map<String, String> COLOR_CODES = Map.of(
            "blue", Colors.BLUE,
            "green", Colors.GREEN,
            "gray", Colors.GRAY,
            "purple", Colors.PURPLE,
            "orangered", Colors.ORANGE_RED,
            "red", Colors.RED,
            "orange", Colors.ORANGE,
            "yellow", Colors.YELLOW,
            "white", "<white>"
    );

    private static String applyColors(String message) {
        for (Map.Entry<String, String> entry : COLOR_CODES.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }
}
