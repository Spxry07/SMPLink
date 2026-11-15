package net.spxry.smplink.api;

public final class SMPLinkProvider {

    private static SMPLinkAPI api;

    private SMPLinkProvider() {}

    public static SMPLinkAPI get() {
        return api;
    }

    public static void register(SMPLinkAPI instance) {
        api = instance;
    }

    public static void unregister() {
        api = null;
    }
}
