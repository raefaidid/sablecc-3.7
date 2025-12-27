// Wrapper class for ints which lets you change the value
// Needed to implement attributed grammars with recursive descent

 

class MutableInt extends Object
{

int value;

MutableInt (int i)
{  value = i; }

MutableInt ()
{  value = 0;		// default
}

int get()
{ return value; }

void set(int i)
{ value = i; }

void set (MutableInt m)
{ value = m.get(); }

public String toString()
{  return (new Integer (value)).toString(); }

}