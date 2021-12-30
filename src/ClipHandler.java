import javax.sound.sampled.*;

public class ClipHandler {
    private static final Clip[] clips = new Clip[5];   // 4 = fail sound
    static
    {
        for (int s=0; s<5; s++)
        {
            String name;
            try {
                clips[s] = AudioSystem.getClip();
                if (s < 4)
                    name = "snd"+(s+1)+".wav";
                else
                    name = "lose.wav";
                clips[s].open (AudioSystem.getAudioInputStream(Utils.getResource(name)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void play (int num) {
        try {
            clips[num].setMicrosecondPosition(0);
            clips[num].start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
