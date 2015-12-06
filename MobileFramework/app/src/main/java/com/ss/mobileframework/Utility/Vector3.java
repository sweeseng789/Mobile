package com.ss.mobileframework.Utility;

/**
 * Created by sweeseng789 on 5/12/2015.
 */
public class Vector3
{
    public float x, y, z;
    final double EPSILON = Float.MIN_VALUE;

    public Vector3()
    {
        setZero();
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = y;
    }

    public Vector3(Vector3 pos)
    {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setZero()
    {
        x = y = z = 0.f;
    }

    public boolean isEqual(float a, float b)
    {
        return a - b <= EPSILON && b - a <= EPSILON;
    }

    public boolean isZero()
    {
        return isEqual(x, 0.f) && isEqual(y, 0.f) && isEqual(z, 0.f);
    }

    public float length()
    {
        return (float)(Math.sqrt(x * x + y * y + z * z));
    }

    public float lengthSquared()
    {
        return x * x + y * y + z * z;
    }

    public float dot(Vector3 rhs)
    {
        return x * rhs.x + y * rhs.y + z * rhs.z;
    }

    public Vector3 cross(Vector3 rhs)
    {
        Vector3 returnV3 = new Vector3();
        returnV3.x = y * rhs.z - z * rhs.y;
        returnV3.y = z * rhs.x - x * rhs.z;
        returnV3.z = x * rhs.x - y * rhs.x;

        return returnV3;
    }

    public Vector3 normalized()
    {
        float d = length();
        Vector3 returnV3 = new Vector3();
        returnV3.x = x / d;
        returnV3.y = y / d;
        returnV3.z = z / d;

        return  returnV3;
    }

    public Vector3 normalize()
    {
        float d = length();

        x /= d;
        y /= d;
        z /= d;

        return this;
    }
}
