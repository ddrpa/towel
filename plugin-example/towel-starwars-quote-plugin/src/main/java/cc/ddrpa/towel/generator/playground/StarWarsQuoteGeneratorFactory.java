package cc.ddrpa.towel.generator.playground;

import cc.ddrpa.towel.ColumnDetail;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

import java.security.SecureRandom;

public class StarWarsQuoteGeneratorFactory implements IGeneratorFactory {
    private static final String description = "星球大战系列电影人物台词";
    private static final String usage = "just do it.";

    @Override
    public IGenerator build(ColumnDetail columnDetail) {
        return new StarWarsQuoteGenerator();
    }

    @Override
    public String getName() {
        return "ddrpa:starwars-quote";
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    public static class StarWarsQuoteGenerator implements IGenerator {
        private static final SecureRandom random = new SecureRandom();
        private static final String[] quotes = new String[]{
                "Try not. Do or do not. There is no try.",
                "Your eyes can deceive you; don’t trust them.",
                "Luminous beings we are, not this crude matter.",
                "Who’s the more foolish: the fool or the fool who follows him?",
                "Your focus determines your reality.",
                "No longer certain that one ever does win a war, I am.",
                "In a dark place we find ourselves and a little more knowledge lights our way.",
                "Sometimes we must let go of our pride and do what is requested of us.",
                "We’ll always be with you. No one’s ever really gone. A thousand generations live in you now.",
                "The ability to speak does not make you intelligent.",
                "Difficult to see; always in motion is the future.",
                "Many of the truths that we cling to depend on our viewpoint.",
                "Train yourself to let go of everything you fear to lose.",
                "I like firsts. Good or bad, they're always memorable."
        };

        @Override
        public String next() {
            return quotes[random.nextInt(14)];
        }
    }
}