package dk.kiljacken.aestuscraft.library;

public class SquareSpiral {
    private int m_SpiralX;
    private int m_SpiralZ;
    private int m_SpiralSide;
    private int m_SpiralSideLenth;
    private int m_SpiralISide;
    private int m_SpiralI;
    private int m_SpiralIMax;

    public SquareSpiral(int iMax)
    {
        resetSpiral();
        m_SpiralIMax = iMax;
    }

    public SquareSpiral()
    {
        this(Integer.MAX_VALUE);
    }

    public void updateSpiral()
    {
        m_SpiralI++;
        m_SpiralISide++;

        switch (m_SpiralSide)
        {
        case 0:
            m_SpiralX++;
            break;
        case 1:
            m_SpiralZ++;
            break;
        case 2:
            m_SpiralX--;
            break;
        case 3:
            m_SpiralZ--;
            break;
        }

        if (m_SpiralISide == m_SpiralSideLenth)
        {
            m_SpiralSide = (m_SpiralSide + 1) % 4;

            if (m_SpiralSide % 2 == 0)
            {
                m_SpiralSideLenth++;
            }

            m_SpiralISide = 0;
        }

        if (m_SpiralI >= m_SpiralIMax)
        {
            resetSpiral();
        }
    }

    public void resetSpiral()
    {
        m_SpiralX = 0;
        m_SpiralZ = 0;
        m_SpiralSide = 0;
        m_SpiralSideLenth = 1;
        m_SpiralISide = 0;
        m_SpiralI = 0;
    }

    public int getSpiralX()
    {
        return m_SpiralX;
    }

    public int getSpiralZ()
    {
        return m_SpiralZ;
    }
}
