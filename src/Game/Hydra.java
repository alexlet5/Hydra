package Game;

public class Hydra
{
    char type;
    char rotation;

    public void spawn()
    {
        this.type = 's';
    }
    void body(char r)
    {
        this.type = 'b';
        this.rotation = 'r';
    }

    public void toBody()
    {
        this.type = 'b';
    }

    public boolean isHead()
    {
        return type == 'h';
    }

    public void die()
    {
        this.type = 'd';
    }

    public boolean isBody()
    {
        if(type == 'b' || type == 's') return true;
        else return false;
    }

    public boolean isSomething()
    {
        if(type == 'b' || type == 's' || type == 'h' || type == 'd') return true;
        else return false;
    }

    public boolean isBullet()
    {
        if(type == 'a') return true;
        else return false;
    }


    public char getType()
    {
        return type;
    }

    public void makeHead()
    {
        this.type = 'h';
    }
}
