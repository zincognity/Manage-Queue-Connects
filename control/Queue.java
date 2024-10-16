package control;

public class Queue {
    private Object[] objects;
    private int index;

    public Queue(int length){
        objects = new Object[length];
        index = -1;
    }

    public boolean addObject(Object object){
        if(index == objects.length - 1) {
            return false;
        } else{
            index++;
            objects[index] = object;
            return true;
        }
    }

    public Object deleteObject(){
        Object object = null;

        if(index > -1){
            object = objects[0];
            for (int i = 1; i < index; i++) {
                objects[i-1] = objects[i];
            }
            index--;
        }

        return object;
    }


    public int getSize(){
        return index + 1;
    }
}
