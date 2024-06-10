package Application.Resource;

import Application.ApplicationSystem.Debug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13C.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;

public class SpriteSheet extends Resource
{
    private List<SpriteSheetTexture> m_Textures = new ArrayList<>();

    public void SetSprites(List<SpriteSheetTexture> textures)
    {
        m_Textures = textures;
    }

    public SpriteSheetTexture GetSpriteSheetTexture(int index) { return m_Textures.get(index); }

    public List<SpriteSheetTexture> GetSpritesFromIndexAToIndexB(int indexA, int indexB)
    {
        List<SpriteSheetTexture> textures = new ArrayList<>();

        if(indexA < 0)
            Debug.Error("Invalid index A (lower than 0)");

        if(indexB >= m_Textures.size())
            Debug.Error("Invalid index B (equal to or higher than the amount of SpriteSheetTextures");

        for(int i = indexA; i <= indexB; i++)
        {
            textures.add(m_Textures.get(i));
        }

        return textures;
    }

    public List<SpriteSheetTexture> GetSprites() { return m_Textures; }

    public int AmountOfFrames() { return m_Textures.size(); }

    public static SpriteSheet LoadSpriteSheet(String path, int spriteWidth, int spriteHeight)
    {
        //First pixel has to be at X=0
        //Every sprite has to be with offset of the spriteWidth

        List<SpriteSheetTexture> textures = new ArrayList<>();
        SpriteSheet spriteSheet = new SpriteSheet();

        try
        {
            File imgFile = new File(path);
            BufferedImage bufferedImage = ImageIO.read(imgFile);

            int spriteSheetWidth = bufferedImage.getWidth();
            int spriteSheetHeight = bufferedImage.getHeight();

            for(int y = 0; y < spriteSheetHeight/spriteHeight; y++)
            {
                for(int x = 0; x < spriteSheetWidth/spriteWidth; x++)
                {
                    int Y = y*spriteHeight;
                    int X = x*spriteWidth;

                    BufferedImage subImage = bufferedImage.getSubimage(X, Y, spriteWidth, spriteHeight);
                    if(ImageIsBlank(subImage))
                        continue;

                    int pixels[] = subImage.getRGB(0, 0, spriteWidth, spriteHeight, null, 0, spriteWidth);
                    ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length*4); //*4 because RGBA

                    for(int h = 0; h < spriteHeight; h++)
                    {
                        for(int w = 0; w < spriteWidth; w++)
                        {
                            int pixel = pixels[h * spriteWidth + w];

                            buffer.put((byte) ((pixel >> 16) & 0xFF));
                            buffer.put((byte) ((pixel >> 8) & 0xFF));
                            buffer.put((byte) (pixel & 0xFF));
                            buffer.put((byte) ((pixel >> 24) & 0xFF));
                        }
                    }


                    buffer.flip();

                    int texture = glGenTextures();

                    glBindTexture(GL_TEXTURE_2D, texture);

                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, spriteWidth, spriteHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
                    glGenerateMipmap(GL_TEXTURE_2D);

                    textures.add(new SpriteSheetTexture(texture, x, y));

                    glBindTexture(GL_TEXTURE_2D, 0);
                }
            }
        }
        catch (Exception e)
        {
            Debug.Error("Failed to load sprite sheet from path '" + path + "'");
            e.printStackTrace();
        }

        spriteSheet.SetSprites(textures);
        return spriteSheet;
    }

    private static boolean ImageIsBlank(BufferedImage image)
    {
        int height = image.getHeight();
        int width = image.getWidth();

        int blankPixels = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                int RGB = image.getRGB(x, y);
                if(RGB == 0)
                    blankPixels += 1;
            }
        }

        if(blankPixels >= (width * height))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
