package ru.chancearea.servomotionscontrolpanel.utils;

public abstract class MathPlus {

    // ------------- Numbers ------------
    public static float roundTo(float _number, int _count) {
        float ten_power = (float) Math.pow(10, (float) _count);
        return (Math.round(_number * ten_power) / ten_power);
    }

    public static double roundTo(double _number, long _count) {
        double ten_power = Math.pow(10, (double) _count);
        return (Math.round(_number * ten_power) / ten_power);
    }

    // --------- For threads --------
    public static long calculateMillisToSleep(int _numAttempts) {
        return Math.round( (1000 / (_numAttempts + Math.exp(-_numAttempts))) );
    }


    // ----------- Bytes ----------
    public static int getHighByte(int _value) {
        return ((_value >> 8) & 0xFF);
    }

    public static int getLowByte(int _value) {
        return (_value & 0xFF);
    }

    public static int restoreValueFromHighLowBytes(int _high, int _low) {
        int restoredValue = (_high << 8) | (_low & 0xFF);
        if ((restoredValue & 0x8000) != 0) restoredValue |= 0xFFFF_0000;

        return restoredValue;
    }

    // ----------- Check sum ------------
    public static int[] calculateChecksum(int[] _data) {
        int index  = 0;
        int regCRC = 0xFFFF;

        while (index != (_data.length - 2)) {
            regCRC ^= _data[index];

            for (byte i = 0; i < 8; i++) {
                regCRC = ((regCRC & 0x01) == 1 ? (regCRC >> 1) ^ 0xA001 : regCRC >> 1);
            }

            index++;
        }

        return ( new int[]{getLowByte(regCRC), getHighByte(regCRC)} );
    }
}
