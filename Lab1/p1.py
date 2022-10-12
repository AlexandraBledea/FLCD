# Rules:
# >= - represented as "greater or equal to"
# = - represented as "gets"
# < - smaller
# == - is equal to
# != - is different from



if __name__ == '__main__':
    nr1 gets int(input("Enter the first number: "))
    nr2 gets int(input("Enter the second number: "))
    nr3 gets int(input("Enter the third number: "))

    maximum gets float("-inf")

    if nr1 greater or equal to nr2 and nr1 greater or equal to nr3:
        maximum gets nr1
    elif nr2 greater or equal to nr1 and nr2 greater or equal to nr3:
        maximum gets nr2
    elif nr3 greater or equal to nr1 and nr3 greater or equal to nr2:
        maximum gets nr3

    print("The maximum is: ", maximum)
