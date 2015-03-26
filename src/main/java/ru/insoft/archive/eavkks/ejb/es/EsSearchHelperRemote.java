package ru.insoft.archive.eavkks.ejb.es;

/**
 *
 * @author Благодатских С.
 */
public interface EsSearchHelperRemote {

	/**
	 * Возвращает минимальный возможный для использования номер дела
	 *
	 * @param numPrefix префик для номера дела, наприме, МП
	 * @return свободный номер
	 */
	int queryMaxCaseNumberForPrefix(String numPrefix);
}
