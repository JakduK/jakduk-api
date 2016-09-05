package com.jakduk.batch.processor;

import com.jakduk.batch.model.BoardFree;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 9. 5.
 */
public class MyItemProcessor implements ItemProcessor<BoardFree, BoardFree> {

	/**
	 * Process the provided item, returning a potentially modified or new item for continued
	 * processing.  If the returned result is null, it is assumed that processing of the item
	 * should not continue.
	 *
	 * @param item to be processed
	 * @return potentially modified or new item for continued processing, null if processing of the
	 * provided item should not continue.
	 * @throws Exception
	 */
	@Override public BoardFree process(BoardFree item) throws Exception {
		System.out.println("item=" + item);
		return item;
	}
}
