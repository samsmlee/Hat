package com.simtuit.hat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class VoteContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Vote> ITEMS = new ArrayList<Vote>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Long, Vote> ITEM_MAP = new HashMap<>();

    protected static long currID = 0;

    private static void addVote(Vote item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        currID++;
    }
    public static void addVote(String content)
    {
        addVote(new Vote(getNextID(), content));
    }

    protected static long getNextID()
    {

        return currID++;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Vote {
        public long id;
        public String content;

        public Vote(long id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
