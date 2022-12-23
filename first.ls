welcome()

### Global Variables
# In Operational, global variables are surrounded by [square brackets].
# Globals can be accessed from anywhere, including other scripts.
[value]: Int = 10

### Functions
# We can declare a function by one of the two following examples.
# Anything is accepted if it matches the regex fu?n [name](argName: Type...) { ... }
fn foo(message: String) {
    broadcast message
}

fn bar(message: String, times: Int) {
    repeat(times) {
        broadcast message
    }
}

fn test(something: Int) {

}
# todo function calls dont actually work lol ecs dee
# Calling a function is exactly how you would expect.
#bar("Hello, world!", 2)

# If a function only has one parameter then we can invoke it without brackets.
#foo "Foo call"

println "[value] is currently %[value]%"
[value] = "hello world"
println "[value] is now %[value]%"

### Local Variables
# Local variables only exist in the block of code
# In this instance this variable "local1" won't exist after the final }
fn locals(local1: String) {
    println local1
}

### Event Listeners
# To communicate with the JVM, we can fire events via a string ID,
# and create listeners for them too.
on<joinEvent> {
    println event
}

# Fire an event within the script
fireEvent("joinEvent")

message: String = "Hello world, this is cool"
num1 = -2
num2 = 3
message = "Hello world, %message%"

list = ["val1", "val2"]

for (list) {
    message = "%message% [%loopVal%]"
}
println message

# TODO
# - Be able to return function values
# - Use functions and vars instead of hard values