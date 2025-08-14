package de.thm.mcptest.security;

public class McpUserHolder {
    private static final InheritableThreadLocal<String> holder = new InheritableThreadLocal<>();

    public static void set(String token) {
        holder.set(token);
    }

    public static String get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}