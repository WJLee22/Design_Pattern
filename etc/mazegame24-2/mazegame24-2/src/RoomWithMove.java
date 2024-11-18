import java.util.Random;

public class RoomWithMove extends Room {
    public RoomWithMove(int roomNumber) {
        super(roomNumber);
    }

    @Override
    public void enter(Maze maze) {
        MazeController controller = MazeController.getInstance();
        controller.process("랜덤방으로 이동되었습니다~!!!");

        int randomRoomIndex = (int) (Math.random() * maze.rooms.size());
        Room randomRoom = maze.rooms.get(randomRoomIndex);
        //maze.setCurrentRoom(randomRoom);
        randomRoom.enter(maze);
    }
}