import javax.sound.sampled.*;

public class ClipHandler {
    private static final Clip[] clips = new Clip[6];   // 4 = fail sound
    static
    {
        for (int s=0; s<clips.length; s++)
        {
            try {
                clips[s] = AudioSystem.getClip();
                if (s == 4)
                    clips[s].open (AudioSystem.getAudioInputStream(Utils.getResource("lose.wav")));
                else if (s == 5)
                    clips[s].open (AudioSystem.getAudioInputStream(Utils.getResource("silence.wav")));
                else
                    clips[s].open (AudioSystem.getAudioInputStream(Utils.getResource("snd"+(s+1)+".wav")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void play (int num) {
            Clip cl = clips[num];
        try {
            cl.stop();
            cl.setMicrosecondPosition(0);
            cl.start();
            while(cl.getMicrosecondLength() != cl.getMicrosecondPosition())
            {
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
