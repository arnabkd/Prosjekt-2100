        .globl  one                     
one:    pushl   %ebp                    # Start function one
        movl    %esp,%ebp               
        movl    8(%ebp),%eax            # a
        pushl   %eax                    
        movl    12(%ebp),%eax           # b
        movl    %eax,%ecx               
        popl    %eax                    
        imull   %ecx,%eax               # Compute *
        jmp     .exit$one               # return-statement
.exit$one:                                
        popl    %ebp                    
        ret                             # End function one
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $12,%esp                # Get 12 bytes local data space
        movl    $1,%eax                 # 1
        movl    %eax,-4(%ebp)           # k =
        movl    -4(%ebp),%eax           # k
        pushl   %eax                    # Push parameter #2
        movl    -4(%ebp),%eax           # k
        pushl   %eax                    # Push parameter #1
        call    one                     # Call one
        popl    %ecx                    # Pop parameter #1
        popl    %ecx                    # Pop parameter #2
        movl    %eax,-8(%ebp)           # x =
        movl    -8(%ebp),%eax           # x
        pushl   %eax                    # Push parameter #2
        movl    -8(%ebp),%eax           # x
        pushl   %eax                    
        movl    -4(%ebp),%eax           # k
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    # Push parameter #1
        call    one                     # Call one
        popl    %ecx                    # Pop parameter #1
        popl    %ecx                    # Pop parameter #2
        pushl   %eax                    # Push parameter #2
        movl    -8(%ebp),%eax           # x
        pushl   %eax                    # Push parameter #1
        call    one                     # Call one
        popl    %ecx                    # Pop parameter #1
        popl    %ecx                    # Pop parameter #2
        movl    %eax,-12(%ebp)          # f =
.exit$main:
        addl    $12,%esp                # Release local data space
        popl    %ebp                    
        ret                             # End function main
