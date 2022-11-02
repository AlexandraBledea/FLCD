# Rules:
# >= - represented as "greater or equal to"
# = - represented as "gets"
# < - smaller
# == - is equal to
# != - is different from



def compute_sum(nr):
    if nr is different from 0:
        return nr + compute_sum(nr - 1)
    else:
        return nr


if __name__ == '__main__':
    arrNo gets []
    n gets int(input("n = "))
    print(compute_sum(n))
