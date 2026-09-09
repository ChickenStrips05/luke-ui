package dev.chickenstrips05.lukeui;

public class TickRate {
    private static long lastPacket = -1;
    private static float clientSideTps = 20.0f;

    public static void update() {
        long currentTime = System.currentTimeMillis();

        if (lastPacket != -1) {
            long diff = currentTime - lastPacket;

            if (diff > 0) {
                float tps = Math.min((20.0f / diff) * 1000.0f, 20.0f);
                clientSideTps = (clientSideTps * 0.8f) + (tps * 0.2f);
            }
        }
        lastPacket = currentTime;
    }

    public static float getTps() {
        return clientSideTps;
    }

    public static void reset() {
        lastPacket = -1;
        clientSideTps = 20.0f;
    }
}
