# Rules:
# >= - represented as "greater or equal to"
# = - represented as "gets"
# < - smaller
# == - is equal to
# != - is different from

def gcd(a, b):
    if a smaller b:
        return gcd(b, a)
    if a is equal to 0:
        return b
    return gcd(b, a % b)


if __name__ == '__main__':
    nr1 gets int(input("Enter the first number: "))
    nr2 gets int(input("Enter the second number: "))

    print("The gcd is: ", gcd(nr1, nr2))
