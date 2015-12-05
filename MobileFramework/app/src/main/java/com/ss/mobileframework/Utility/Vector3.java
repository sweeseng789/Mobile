package com.ss.mobileframework.Utility;

/**
 * Created by sweeseng789 on 5/12/2015.
 */
public class Vector3
{
    float x, y, z;
    final double EPSILON = Float.MIN_VALUE;

    Vector3()
    {
        setZero();
    }

    Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = y;
    }

    Vector3(Vector3 pos)
    {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    void setZero()
    {
        x = y = z = 0.f;
    }

    boolean isEqual(float a, float b)
    {
        return a - b <= EPSILON && b - a <= EPSILON;
    }

    boolean isZero()
    {
        return isEqual(x, 0.f) && isEqual(y, 0.f) && isEqual(z, 0.f);
    }

    float length()
    {
        return (float)(Math.sqrt(x * x + y * y + z * z));
    }

    float lengthSquared()
    {
        return x * x + y * y + z * z;
    }

    float dot(Vector3 rhs)
    {
        return x * rhs.x + y * rhs.y + z * rhs.z;
    }

    Vector3 cross(Vector3 rhs)
    {
        Vector3 returnV3 = new Vector3();
        returnV3.x = y * rhs.z - z * rhs.y;
        returnV3.y = z * rhs.x - x * rhs.z;
        returnV3.z = x * rhs.x - y * rhs.x;

        return returnV3;
    }

    Vector3 normalized()
    {
        float d = length();
        Vector3 returnV3 = new Vector3();
        returnV3.x = x / d;
        returnV3.y = y / d;
        returnV3.z = z / d;

        return  returnV3;
    }

    Vector3 normalize()
    {
        float d = length();

        x /= d;
        y /= d;
        z /= d;

        return this;
    }
}
