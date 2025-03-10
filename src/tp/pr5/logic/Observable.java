package tp.pr5.logic;

public interface Observable<T> {
	
	// Adds an observer
	void addObserver(T o);
	
	// Removes an observer
	void removeObserver(T o);
	
}
