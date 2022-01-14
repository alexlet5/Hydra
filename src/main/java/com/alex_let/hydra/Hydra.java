package com.alex_let.hydra;

public class Hydra
{
    BodyTypes type;

    public Hydra()
    {
        this.type = BodyTypes.EMPTY;
    }

    public void spawn()
    {
        this.type = BodyTypes.HOLE;
    }

    public void toBody()
    {
        type = BodyTypes.BODY;
    }

    public void toDead()
    {
        this.type = BodyTypes.DEAD;
    }

    public void makeHead() throws Exception
    {
        if (this.type == BodyTypes.EMPTY)
            this.type = BodyTypes.HEAD;
        else
            throw new Exception("Hydra already has something in this cell");
    }

    public boolean isBody()
    {
        return type.equals(BodyTypes.BODY);
    }

    public boolean isSomething()
    {
        return !type.equals(BodyTypes.EMPTY);
    }

    public boolean isHead()
    {
        return type.equals(BodyTypes.HEAD);
    }

    public boolean isHole()
    {
        return type.equals(BodyTypes.HOLE);
    }

    public boolean isDead(){ return type.equals(BodyTypes.DEAD);}

    public BodyTypes getState(){ return type;}

    private enum BodyTypes
    {
        EMPTY, HEAD, DEAD, BODY, HOLE
    }
}
