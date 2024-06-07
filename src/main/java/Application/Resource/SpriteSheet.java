package Application.Resource;

import Application.ApplicationSystem.Debug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.*;
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

    public int AmountOfFrames() { return m_Textures.size(); }

    public static SpriteSheet LoadSpriteSheet(String path, int spriteWidth, int spriteHeight)
    {
        List<SpriteSheetTexture> textures = new ArrayList<>();
        SpriteSheet spriteSheet = new SpriteSheet();

        try
        {
            File img = new File(path);
            BufferedImage bufferedImage = ImageIO.read(img);

            int spriteSheetWidth = bufferedImage.getWidth();
            int spriteSheetHeight = bufferedImage.getHeight();

            int X = 0;
            int Y = 0;

            for(int x = 0; x < spriteSheetWidth; x++)
            {
                for(int y = 0; y < spriteSheetHeight; y++)
                {
                    BufferedImage IMAGE = bufferedImage.getSubimage(X, Y, spriteWidth, spriteHeight);
                    if(ImageIsBlank(IMAGE))
                    {
                        X += spriteWidth;
                        if(X >= spriteSheetWidth)
                        {
                            X = 0;
                            Y += spriteHeight;
                        }
                        if(Y >= spriteSheetHeight)
                        {
                            spriteSheet.SetSprites(textures);
                            return spriteSheet;
                        }
                    }

                    int IMAGE_WIDTH = IMAGE.getWidth();
                    int IMAGE_HEIGHT = IMAGE.getHeight();
                    int pixels[] = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
                    IMAGE.getRGB(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, pixels, 0, IMAGE_HEIGHT);
                    ByteBuffer buffer = ByteBuffer.allocateDirect(IMAGE_WIDTH*IMAGE_HEIGHT*4);

                    for(int h = 0; h < IMAGE_HEIGHT; h++)
                    {
                        for(int w = 0; w < IMAGE_HEIGHT; w++)
                        {
                            int pixel = pixels[h * IMAGE_WIDTH + w];

                            buffer.put((byte) ((pixel >> 16) & 0xFF));
                            buffer.put((byte) ((pixel >> 8) & 0xFF));
                            buffer.put((byte) (pixel & 0xFF));
                            buffer.put((byte) ((pixel >> 24) & 0xFF));
                        }
                    }

                    buffer.flip();

                    int texture = glGenTextures();

                    glBindTexture(GL_TEXTURE_2D, texture);
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, IMAGE_WIDTH, IMAGE_HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
                    glGenerateMipmap(GL_TEXTURE_2D);

                    textures.add(new SpriteSheetTexture(texture, X, Y));

                    glBindTexture(GL_TEXTURE_2D, 0);

                    X += spriteWidth;
                    if(X >= spriteSheetWidth)
                    {
                        X = 0;
                        Y += spriteHeight;
                    }
                    if(Y >= spriteSheetHeight)
                    {
                        x = spriteSheetWidth;
                        y = spriteSheetHeight;
                    }
                }
            }
        }
        catch (IOException e)
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
