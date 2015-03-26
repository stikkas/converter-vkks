package ru.insoft.archive.eavkks.load.ejb;

/**
 *
 * @author Благодатских С.
 */
public interface LoaderRemote {
	String loadRemote(String srcDir, boolean clear);
}
