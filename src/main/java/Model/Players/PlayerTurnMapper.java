package Model.Players;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapea los turnos originales de los jugadores a turnos actuales
 * cuando hay jugadores eliminados
 */
public class PlayerTurnMapper {
    private Map<Integer, Integer> originalToCurrentTurn;
    private int nextAvailableTurn;

    public PlayerTurnMapper() {
        this.originalToCurrentTurn = new HashMap<>();
        this.nextAvailableTurn = 1;
    }

    /**
     * Registra un jugador con su turno original
     */
    public synchronized void registerPlayer(int originalTurn) {
        originalToCurrentTurn.put(originalTurn, nextAvailableTurn);
        nextAvailableTurn++;
        System.out.println("📋 Jugador registrado: turno original " + originalTurn + " → turno actual " + originalToCurrentTurn.get(originalTurn));
    }

    /**
     * Elimina un jugador y reorganiza los turnos
     */
    public synchronized void removePlayer(int originalTurn) {
        Integer removedTurn = originalToCurrentTurn.remove(originalTurn);
        if (removedTurn == null) {
            System.out.println("⚠️ Jugador con turno " + originalTurn + " no encontrado");
            return;
        }

        System.out.println("❌ Eliminando jugador turno original " + originalTurn + " (turno actual " + removedTurn + ")");

        // Reorganizar los turnos de los jugadores con turno mayor al eliminado
        for (Map.Entry<Integer, Integer> entry : originalToCurrentTurn.entrySet()) {
            if (entry.getValue() > removedTurn) {
                entry.setValue(entry.getValue() - 1);
                System.out.println("   → Jugador turno original " + entry.getKey() + " ahora tiene turno " + entry.getValue());
            }
        }

        nextAvailableTurn--;
    }

    /**
     * Obtiene el turno actual de un jugador basado en su turno original
     */
    public synchronized int getCurrentTurn(int originalTurn) {
        return originalToCurrentTurn.getOrDefault(originalTurn, -1);
    }

    /**
     * Verifica si un jugador sigue en juego
     */
    public synchronized boolean isPlayerActive(int originalTurn) {
        return originalToCurrentTurn.containsKey(originalTurn);
    }

    /**
     * Obtiene el número de jugadores activos
     */
    public synchronized int getActivePlayers() {
        return originalToCurrentTurn.size();
    }
}